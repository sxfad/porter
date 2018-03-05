/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.extract;

import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.datacarrier.DataCarrier;
import com.suixingpay.datas.node.datacarrier.DataCarrierFactory;
import com.suixingpay.datas.node.task.extract.extractor.ExtractorFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 完成事件的进一步转换、过滤。多线程执行
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class ExtractJob extends AbstractStageJob {
    private static final int BUFFER_SIZE = LOGIC_THREAD_SIZE * 10;
    private final TaskWork work;
    private final ExecutorService executorService;
    private final DataCarrier<ETLBucket> carrier;
    private final DataCarrier<String> orderedBucket;
    private final ExtractorFactory extractorFactory;
    public ExtractJob(TaskWork work) {
        super(work.getBasicThreadName(), 1000L);
        extractorFactory = NodeContext.INSTANCE.getBean(ExtractorFactory.class);
        this.work = work;
        //线程阻塞时，在调用者线程中执行
        executorService = new ThreadPoolExecutor(LOGIC_THREAD_SIZE, LOGIC_THREAD_SIZE,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(LOGIC_THREAD_SIZE * 2),
                getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        carrier = NodeContext.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE, 1);
        orderedBucket = NodeContext.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE, 1);
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
        Pair<String, List<MessageEvent>> events = null;
        do {
            try {
                events = work.waitEvent(StageType.SELECT);
                if (null != events && !events.getRight().isEmpty()) {
                    final Pair<String, List<MessageEvent>> inThreadEvents = events;
                    LOGGER.debug("extract MessageEvent batch {}.", inThreadEvents.getLeft());
                    //在单线程执行，保证将来DataLoader load顺序
                    orderedBucket.push(inThreadEvents.getLeft());
                    //暂无Extractor失败处理方案
                    executorService.submit(() -> {
                        //将MessageEvent转换为ETLBucket
                        ETLBucket bucket = ETLBucket.from(inThreadEvents);
                        try {
                            extractorFactory.extract(bucket, work.getDataConsumer().getExcludes(), work.getDataConsumer().getIncludes());
                            carrier.push(bucket);
                            LOGGER.debug("push bucket {} into carrier after extract.", inThreadEvents.getLeft());
                        } catch (Exception e) {
                            bucket.tagException(new TaskStopTriggerException(e));
                            LOGGER.error("批次[{}]执行ExtractJob失败!", inThreadEvents.getLeft(), e);
                        }
                    });
                }
            } catch (Exception e) {
                NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(),  work.getDataConsumer().getSwimlaneId(),
                        "extract MessageEvent error" + e.getMessage());
                LOGGER.error("extract MessageEvent error!", e);
            }
        } while (null != events && null != events.getRight() && !events.getRight().isEmpty());
    }

    @Override
    public ETLBucket output() {
        return carrier.pull();
    }
    public <T> T getNextSequence() {
        return orderedBucket.pull();
    }

    @Override
    public boolean isPoolEmpty() {
        return orderedBucket.size() == 0 && carrier.size() == 0 && executorService.isTerminated();
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.SELECT);
    }

    @Override
    public boolean stopWaiting() {
        return work.getDataConsumer().isAutoCommitPosition();
    }
}