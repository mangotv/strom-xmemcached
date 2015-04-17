package net.rubyeye.xmemcached.impl;

import java.io.Serializable;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: AM11:56
 */
public class ThreadLocalBinarySetQCollector extends ThreadLocal<BinarySetQCollector> implements Serializable {
    @Override
    protected BinarySetQCollector initialValue() {
        return new BinarySetQCollector();
    }
}
