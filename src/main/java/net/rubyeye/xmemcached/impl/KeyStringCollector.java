package net.rubyeye.xmemcached.impl;

import net.rubyeye.xmemcached.command.Command;

import java.io.Serializable;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: AM11:30
 */
public class KeyStringCollector implements CommandCollector, Serializable {
    char[] buf = new char[1024 * 2];
    int count = 0;
    boolean wasFirst = true;

    public CommandCollector reset() {
        this.count = 0;
        this.wasFirst = true;
        return this;
    }

    public Object getResult() {
        return new String(this.buf, 0, this.count);
    }

    public void visit(Command command) {
        if (this.wasFirst) {
            this.append(command.getKey());
            this.wasFirst = false;
        } else {
            this.append(" ");
            this.append(command.getKey());
        }
    }

    private void expandCapacity(int minimumCapacity) {
        int newCapacity = (this.buf.length + 1) * 2;
        if (newCapacity < 0) {
            newCapacity = Integer.MAX_VALUE;
        } else if (minimumCapacity > newCapacity) {
            newCapacity = minimumCapacity;
        }
        char[] copy = new char[newCapacity];
        System.arraycopy(this.buf, 0, copy, 0, Math.min(this.buf.length, newCapacity));
        this.buf = copy;
    }

    private void append(String str) {
        int len = str.length();
        if (len == 0) {
            return;
        }
        int newCount = this.count + len;
        if (newCount > this.buf.length) {
            this.expandCapacity(newCount);
        }
        str.getChars(0, len, this.buf, this.count);
        this.count = newCount;
    }

    public void finish() {
        // do nothing

    }

}
