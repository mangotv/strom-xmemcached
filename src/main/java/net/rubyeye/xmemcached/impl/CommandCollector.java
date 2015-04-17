package net.rubyeye.xmemcached.impl;

import net.rubyeye.xmemcached.command.Command;

import java.io.Serializable;

/**
 * Copyright (c) 2012, Sohu.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: AM11:29
 */
public interface CommandCollector extends Serializable {
    public Object getResult();

    public void visit(Command command);

    public void finish();

    public CommandCollector reset();
}
