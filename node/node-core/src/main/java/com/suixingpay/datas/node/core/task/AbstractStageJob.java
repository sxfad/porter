package com.suixingpay.datas.node.core.task;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 阶段性工作
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:04
 */
public abstract class AbstractStageJob implements StageJob {
    protected  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    protected static final int LOGIC_THREAD_SIZE = 5;
    private AtomicBoolean stat = new AtomicBoolean(false);
    private final Thread loopService;
    private final ThreadFactory threadFactory;
    public AbstractStageJob(String baseThreadName) {
        threadFactory = new DefaultNamedThreadFactory(baseThreadName + "-" + this.getClass().getSimpleName());
        loopService = threadFactory.newThread(new LoopService());
    }
    protected abstract void doStop();

    protected abstract void doStart();

    @Override
    public void start() {
        if (canStart() && stat.compareAndSet(false, true)) {
            try {
                doStart();
                loopService.start();
            } catch (Exception e) {
                stop();
            }
        } else {

        }
    }



    @Override
    public void stop() {
        if (stat.compareAndSet(true, false)) {
            try {
                loopService.interrupt();
                doStop();
            } catch (Exception e) {
            }
        } else {

        }
    }

    protected abstract void loopLogic();

    private  class LoopService implements Runnable {
        @Override
        public void run() {
            //如果线程没有中断信号，持续执行
            while (!Thread.currentThread().isInterrupted()) {
                loopLogic();
                //不符合业务执行条件时，线程沉睡10秒后继续执行
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {//如果线程有中断信号，退出线程
                    break;
                }
            }
        }
    }
    protected ThreadFactory getThreadFactory (){
        return threadFactory;
    }
}
