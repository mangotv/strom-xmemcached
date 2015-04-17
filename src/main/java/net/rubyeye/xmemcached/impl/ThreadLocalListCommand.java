package net.rubyeye.xmemcached.impl;

import net.rubyeye.xmemcached.command.Command;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: PM2:15
 */
public class ThreadLocalListCommand extends ThreadLocal<List<Command>> implements Serializable {

    @Override
    protected List<Command> initialValue() {
        return new ArrayList<Command>(Optimizer.mergeFactor);
    }
}
