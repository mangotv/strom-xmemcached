package net.rubyeye.xmemcached.impl;

import com.google.code.yanf4j.buffer.IoBuffer;
import net.rubyeye.xmemcached.command.Command;
import net.rubyeye.xmemcached.command.CommandType;
import net.rubyeye.xmemcached.command.binary.BinarySetMultiCommand;
import net.rubyeye.xmemcached.command.binary.BinaryStoreCommand;
import net.rubyeye.xmemcached.utils.OpaqueGenerater;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: AM11:37
 */
public class BinarySetQCollector implements CommandCollector, Serializable {
    ArrayList<IoBuffer> bufferList = new ArrayList<IoBuffer>();
    int totalBytes;
    BinaryStoreCommand prevCommand;
    Map<Object, Command> mergeCommands;


    public CommandCollector reset() {
        this.bufferList.clear();
        this.totalBytes = 0;
        this.prevCommand = null;
        this.mergeCommands = null;
        return this;
    }

    public Object getResult() {
        byte[] buf = new byte[this.totalBytes];
        int offset = 0;
        for (IoBuffer buffer : this.bufferList) {
            byte[] ba = buffer.array();
            System.arraycopy(ba, 0, buf, offset, ba.length);
            offset += ba.length;
        }
        BinarySetMultiCommand resultCommand = new BinarySetMultiCommand(
                null, CommandType.SET_MANY, new CountDownLatch(1));
        resultCommand.setIoBuffer(IoBuffer.wrap(buf));
        resultCommand.setMergeCommands(this.mergeCommands);
        resultCommand.setMergeCount(this.mergeCommands.size());
        return resultCommand;
    }

    public void visit(Command command) {

        // Encode prev command
        if (this.prevCommand != null) {
            // first n-1 send setq command
            BinaryStoreCommand setqCmd = new BinaryStoreCommand(
                    this.prevCommand.getKey(), this.prevCommand.getKeyBytes(),
                    CommandType.SET, null, this.prevCommand.getExpTime(),
                    this.prevCommand.getCas(),
                    // set noreply to be true
                    this.prevCommand.getValue(), true,
                    this.prevCommand.getTranscoder());
            // We must set the opaque to get error message.
            int opaque = OpaqueGenerater.getInstance().getNextValue();
            setqCmd.setOpaque(opaque);
            setqCmd.encode();
            this.totalBytes += setqCmd.getIoBuffer().remaining();


            this.bufferList.add(setqCmd.getIoBuffer());
            // GC friendly
            setqCmd.setIoBuffer(MemcachedHandler.EMPTY_BUF);
            setqCmd.setValue(null);
            this.prevCommand.setValue(null);
            this.prevCommand.setIoBuffer(MemcachedHandler.EMPTY_BUF);
            if (this.mergeCommands == null) {
                this.mergeCommands = new HashMap<Object, Command>();
            }
            this.mergeCommands.put(opaque, this.prevCommand);
        }
        this.prevCommand = (BinaryStoreCommand) command;
    }

    public void finish() {
        if (this.mergeCommands == null) {
            return;
        }
        // prevCommand is the last command,last command must be a SET
        // command,ensure
        // previous SETQ commands sending response back
        BinaryStoreCommand setqCmd = new BinaryStoreCommand(
                this.prevCommand.getKey(), this.prevCommand.getKeyBytes(),
                CommandType.SET, null, this.prevCommand.getExpTime(),
                this.prevCommand.getCas(),
                // set noreply to be false.
                this.prevCommand.getValue(), false, this.prevCommand.getTranscoder());
        // We must set the opaque to get error message.
        int opaque = OpaqueGenerater.getInstance().getNextValue();
        setqCmd.setOpaque(opaque);
        setqCmd.encode();
        this.bufferList.add(setqCmd.getIoBuffer());
        this.totalBytes += setqCmd.getIoBuffer().remaining();
        if (this.mergeCommands != null) {
            this.mergeCommands.put(opaque, this.prevCommand);
        }
    }

}

