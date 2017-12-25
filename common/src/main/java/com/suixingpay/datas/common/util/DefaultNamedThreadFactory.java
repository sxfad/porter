package com.suixingpay.datas.common.util;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 14:53
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 14:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 14:53
 */
public class DefaultNamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger JOB_SERVICE_SEQ = new AtomicInteger(0);
    private final AtomicInteger threadSeq = new AtomicInteger(0);
    private final String namePrefix;
    public DefaultNamedThreadFactory(String name) {
        namePrefix = "suixingpay-" + JOB_SERVICE_SEQ.incrementAndGet() + "-" + name + "-";
    }

    @Override
    public Thread newThread(Runnable r) {
        return newThread(r, threadSeq.getAndIncrement() + "");
    }

    public Thread newThread(Runnable r,String threadName) {
        Thread t = new Thread(r,namePrefix + threadName);
        if (t.isDaemon()) t.setDaemon(false);
        return t;
    }
}