package net.rubyeye.xmemcached.impl;

import java.io.Serializable;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: PM2:13
 */
public class ThreadLocalBinaryGetQCollector extends ThreadLocal<BinaryGetQCollector> implements Serializable {

    @Override
    public BinaryGetQCollector initialValue() {
        return new BinaryGetQCollector();
    }
}
