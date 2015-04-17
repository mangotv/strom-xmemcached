package net.rubyeye.xmemcached.impl;

import com.google.code.yanf4j.buffer.IoBuffer;
import net.rubyeye.xmemcached.command.Command;
import net.rubyeye.xmemcached.command.CommandType;
import net.rubyeye.xmemcached.command.binary.BinaryGetCommand;
import net.rubyeye.xmemcached.command.binary.BinaryGetMultiCommand;
import net.rubyeye.xmemcached.command.binary.OpCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: AM11:38
 */
public class BinaryGetQCollector implements CommandCollector, Serializable {
    ArrayList<IoBuffer> bufferList = new ArrayList<IoBuffer>(50);
    int totalBytes;
    Command prevCommand;

    public CommandCollector reset() {
        this.bufferList.clear();
        this.totalBytes = 0;
        this.prevCommand = null;
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
        BinaryGetMultiCommand resultCommand = new BinaryGetMultiCommand(
                null, CommandType.GET_MANY, new CountDownLatch(1));
        resultCommand.setIoBuffer(IoBuffer.wrap(buf));
        return resultCommand;
    }

    public void visit(Command command) {
        // Encode prev command
        if (this.prevCommand != null) {
            // first n-1 send getq command
            Command getqCommand = new BinaryGetCommand(
                    this.prevCommand.getKey(), this.prevCommand.getKeyBytes(), null,
                    null, OpCode.GET_KEY_QUIETLY, true);
            getqCommand.encode();
            this.totalBytes += getqCommand.getIoBuffer().remaining();
            this.bufferList.add(getqCommand.getIoBuffer());
        }
        this.prevCommand = command;
    }

    public void finish() {
        // prev command is the last command,last command must be getk,ensure
        // getq commands sending response back
        Command lastGetKCommand = new BinaryGetCommand(
                this.prevCommand.getKey(), this.prevCommand.getKeyBytes(),
                CommandType.GET_ONE, new CountDownLatch(1), OpCode.GET_KEY,
                false);
        lastGetKCommand.encode();
        this.bufferList.add(lastGetKCommand.getIoBuffer());
        this.totalBytes += lastGetKCommand.getIoBuffer().remaining();
    }

}
