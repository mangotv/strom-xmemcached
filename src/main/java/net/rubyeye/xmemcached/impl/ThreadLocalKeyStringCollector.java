package net.rubyeye.xmemcached.impl;

import java.io.Serializable;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: PM2:12
 */
public class ThreadLocalKeyStringCollector extends ThreadLocal<KeyStringCollector> implements Serializable {
    @Override
    public KeyStringCollector initialValue() {
        return new KeyStringCollector();
    }
}
