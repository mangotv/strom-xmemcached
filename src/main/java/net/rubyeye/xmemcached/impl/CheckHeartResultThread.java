package net.rubyeye.xmemcached.impl;

import com.google.code.yanf4j.core.Session;
import com.google.code.yanf4j.util.SystemUtils;
import net.rubyeye.xmemcached.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Copyright (c) 2014, hunantv.com All Rights Reserved.
 * <p/>
 * User: jeffreywu  MailTo:jeffreywu@sohu-inc.com
 * Date: 15/4/17
 * Time: PM3:12
 */
public class CheckHeartResultThread implements Runnable, Serializable {
    private static final Logger log = LoggerFactory
            .getLogger(CheckHeartResultThread.class);
    private static final String HEART_BEAT_FAIL_COUNT_ATTR = "heartBeatFailCount";
    private static final int MAX_HEART_BEAT_FAIL_COUNT = Integer
            .parseInt(System.getProperty("xmemcached.heartbeat.max.fail.times",
                    "3"));

    private final Command versionCommand;
    private final Session session;

    public CheckHeartResultThread(Command versionCommand, Session session) {
        super();
        this.versionCommand = versionCommand;
        this.session = session;
    }

    public void run() {
        try {
            AtomicInteger heartBeatFailCount = (AtomicInteger) this.session
                    .getAttribute(HEART_BEAT_FAIL_COUNT_ATTR);
            if (heartBeatFailCount != null) {
                if (!this.versionCommand.getLatch().await(2000,
                        TimeUnit.MILLISECONDS)) {
                    heartBeatFailCount.incrementAndGet();
                }
                if (this.versionCommand.getResult() == null) {
                    heartBeatFailCount.incrementAndGet();
                } else {
                    // reset
                    heartBeatFailCount.set(0);
                }
                if (heartBeatFailCount.get() > MAX_HEART_BEAT_FAIL_COUNT) {
                    log.warn("Session("
                            + SystemUtils.getRawAddress(this.session
                            .getRemoteSocketAddress())
                            + ":"
                            + this.session.getRemoteSocketAddress()
                            .getPort() + ") heartbeat fail "
                            + heartBeatFailCount.get()
                            + " times,close session and try to heal it");
                    this.session.close();// close session
                    heartBeatFailCount.set(0);
                }
            }
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
