/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.task;

import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Semaphore;
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
    //任务退出信号量，为了保证优雅关机时内存中的数据处理完毕
    private final Semaphore stopSignal;

    //管道无数据线程等待间隙
    private static  final long DEFAULT_THREAD_WAIT_SPAN = 2000;
    private final long threadWaitSpan;

    public AbstractStageJob(String baseThreadName) {
        this(baseThreadName, DEFAULT_THREAD_WAIT_SPAN);
    }
    public AbstractStageJob(String baseThreadName, Long threadWaitSpan) {
        this.threadWaitSpan = null == threadWaitSpan ? DEFAULT_THREAD_WAIT_SPAN : threadWaitSpan;
        stopSignal = new Semaphore(1);
        threadFactory = new DefaultNamedThreadFactory(baseThreadName + "-" + this.getClass().getSimpleName());
        loopService = threadFactory.newThread(new LoopService());
    }
    protected abstract void doStop() throws InterruptedException;

    protected abstract void doStart() throws IOException;

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
                //确保现有数据流处理结束
                if (stopWaiting()) {
                    LOGGER.debug("任务退出线程等待源队列为空.");
                    //判断上层管道是否有未处理完的事件及数据
                    //防止消费间隙上层管道新增数据
                    while (! isPrevPoolEmpty()) {
                        LOGGER.debug("内存队列有未处理完的数据，线程休眠1秒.");
                        Thread.currentThread().sleep(1000L);
                    }
                    //设置信号量的目的是防止loopLogic执行期间代码被粗暴打断
                    stopSignal.acquire();
                    LOGGER.debug("源队列为空，发送线程中断信号");
                }
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
                try {
                    stopSignal.acquire();
                    LOGGER.debug("源队列为空，线程恢复执行.");
                    loopLogic();
                    //不符合业务执行条件时，释放资源。线程沉睡10秒后继续执行
                    stopSignal.release();
                    LOGGER.debug("源队列为空，线程进入等待.");
                    Thread.sleep(threadWaitSpan);
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
