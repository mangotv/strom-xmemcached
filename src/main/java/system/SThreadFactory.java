package system;

import java.io.Serializable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: PM3:18
 */
public class SThreadFactory implements ThreadFactory, Serializable {
    private AtomicInteger threadCounter;
    private String name;


    public SThreadFactory(AtomicInteger threadCounter, String name) {
        this.threadCounter = threadCounter;
        this.name = name;
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, name + "-" + threadCounter.getAndIncrement());
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
