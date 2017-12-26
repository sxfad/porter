package com.suixingpay.datas.node.task.transform;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:32
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.util.ApplicationContextUtils;
import com.suixingpay.datas.node.core.event.ETLBucket;
import com.suixingpay.datas.node.core.event.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.datacarrier.DataCarrier;
import com.suixingpay.datas.node.datacarrier.simple.SimpleDataCarrier;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.*;

/**
 * 多线程执行,完成字段、表的映射转化。
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:32
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:32
 */
public class TransformJob extends AbstractStageJob {
    private static final int BATCH_POOL_SIZE = 512 * (LOGIC_THREAD_SIZE + LOGIC_THREAD_SIZE / 2);

    private final TransformFactory transformFactory;
    private final ExecutorService executorService;
    private final DataCarrier<ETLBucket> carrier;
    private final TaskWork work;
    public TransformJob(TaskWork work) {
        super(work.getBasicThreadName());
        this.work = work;
        transformFactory = ApplicationContextUtils.INSTANCE.getBean(TransformFactory.class);
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(LOGIC_THREAD_SIZE, LOGIC_THREAD_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        carrier = new SimpleDataCarrier(BATCH_POOL_SIZE, 1);
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
                    final ETLBucket inThreadBucket = bucket;
                    Future<Boolean> future = executorService.submit(new Callable<Boolean>() {
                        @Override
                        public Boolean call() {
                            try {
                                transformFactory.transform(inThreadBucket);
                                return true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                    });
                }
            } catch (Exception e) {
                LOGGER.error("transform ETLBucket error!", e);
            }
        } while (null != bucket);
    }

    @Override
    public ETLBucket output() {
        return null;
    }
}
