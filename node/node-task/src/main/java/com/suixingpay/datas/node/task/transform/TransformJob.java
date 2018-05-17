/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.transform;

import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.datacarrier.DataMapCarrier;
import com.suixingpay.datas.node.datacarrier.simple.FixedCapacityCarrier;
import com.suixingpay.datas.node.task.transform.transformer.TransformFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * 多线程执行,完成字段、表的映射转化。
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:32
 */
public class TransformJob extends AbstractStageJob {
    private final TransformFactory transformFactory;
    private final ExecutorService executorService;
    //容量为线程池容量的100倍
    private final DataMapCarrier<String, Future<ETLBucket>> carrier = new FixedCapacityCarrier(JOB_THREAD_SIZE * 100);
    private final TaskWork work;

    //工作线程数量
    private static final int JOB_THREAD_SIZE = 1;

    public TransformJob(TaskWork work) {
        super(work.getBasicThreadName(), 50L);
        this.work = work;
        transformFactory = NodeContext.INSTANCE.getBean(TransformFactory.class);
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(JOB_THREAD_SIZE, JOB_THREAD_SIZE * 3,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(JOB_THREAD_SIZE * 5),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Override
    protected void doStop() {
        executorService.shutdown();
    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() {
        //只要队列有消息，持续读取
        ETLBucket bucket = null;
        do {
            try {
                bucket = work.waitEvent(StageType.EXTRACT);
                if (null != bucket) {
                    LOGGER.debug("transform ETLBucket batch {} begin.", bucket.getSequence());
                    final ETLBucket inThreadBucket = bucket;
                    Future<ETLBucket> result = executorService.submit(() -> {
                        try {
                            //上个流程处理没有异常
                            if (null == inThreadBucket.getException()) {
                                transformFactory.transform(inThreadBucket, work);
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            inThreadBucket.tagException(new TaskStopTriggerException(e));
                            LOGGER.error("批次[{}]执行TransformJob失败!", inThreadBucket.getSequence(), e);
                        }
                        return inThreadBucket;
                    });
                    LOGGER.debug("transform ETLBucket batch {} end.", bucket.getSequence());
                    carrier.push(inThreadBucket.getSequence(), result);
                    carrier.printState();
                }
            } catch (Throwable e) {
                LOGGER.error("transform ETLBucket error!", e);
            }
        } while (null != bucket);
    }

    @Override
    public ETLBucket output() throws ExecutionException, InterruptedException {
        String sequence = work.waitSequence();
        Future<ETLBucket> result = null;
        if (null != sequence) {
            LOGGER.debug("got sequence:{}, Future: {}", sequence, carrier.containsKey(sequence));
            long waitTime = 0;
            //等待该sequence对应的ETLBucket transform完成。捕获InterruptedException异常,是为了保证该sequence能够被处理。
            while (null != sequence && !carrier.containsKey(sequence)) {
                LOGGER.debug("waiting sequence Future:{}", sequence);
                //等待超过5分钟，释放任务
                if (waitTime > 1000 * 60 * 5) {
                    String msg  = "等待批次" + sequence + "SET完成超时(5m)，任务退出。";
                    LOGGER.error(msg);
                    work.stopAndAlarm(msg);
                    break;
                }
                try {
                    waitTime += 50;
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
            }
            LOGGER.debug("got sequence:{}, Future: {}", sequence, carrier.containsKey(sequence));
            result = carrier.pull(sequence);
        }
        return null != result ? result.get() : null;
    }

    @Override
    public boolean isPoolEmpty() {
        return carrier.size() == 0 && executorService.isTerminated();
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.EXTRACT);
    }

    @Override
    public boolean stopWaiting() {
        return work.getDataConsumer().isAutoCommitPosition();
    }
}
