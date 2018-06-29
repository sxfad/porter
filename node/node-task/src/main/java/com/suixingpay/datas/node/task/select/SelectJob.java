/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.select;

import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.datacarrier.DataCarrier;
import com.suixingpay.datas.node.datacarrier.DataCarrierFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

/**
 * 完成SQL事件从数据源的消费。为了提升消费能力，不做过多业务逻辑处理和数据模型转换
 * 获取事件消息，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:15
 */
public class SelectJob extends AbstractStageJob {
    //已无效,实际根据任务配置设置单次处理数据数量
    private static final int PULL_BATCH_SIZE = 100;

    //只需要保证select出来的数据不致使ExtractJob的所有消费线程饥饿即可
    private static final int BUFFER_SIZE = 100;

    private final DataConsumer consumer;
    private final TaskWork work;
    private final DataCarrier<List<MessageEvent>> carrier;
    //最后一次查不到数据时间
    private volatile LocalDate lastNoneFetchTime;
    private volatile LocalDate lastNoneFetchNoticeTime;
    private final long fetchNoticeSpan;
    private final long fetchNoticeThreshould;
    public SelectJob(TaskWork work) {
        super(work.getBasicThreadName(), 50L);
        this.work = work;
        consumer = work.getDataConsumer();
        carrier = NodeContext.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE, PULL_BATCH_SIZE);
        fetchNoticeSpan = work.getDataConsumer().getEmptyFetchNoticeSpan();
        fetchNoticeThreshould = work.getDataConsumer().getEmptyFetchThreshold();
    }

    /**
     * 只有当DataCarrier数据消费完才能退出
     */
    @Override
    protected void doStop() {
        try {
            consumer.shutdown();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStart() throws Exception {
        consumer.startup();
    }

    @Override
    protected void loopLogic() throws InterruptedException {
        //只要队列有消息，持续读取
        List<MessageEvent> events = null;
        do {
            try {
                events = consumer.fetch();
                if (null != events && !events.isEmpty()) {
                    carrier.push(events);
                    lastNoneFetchTime = null;
                }
            } catch (TaskStopTriggerException stopError) {
                stopError.printStackTrace();
                work.stopAndAlarm(stopError.getMessage());
            } catch (InterruptedException interrupt) {
                throw interrupt;
            } catch (Throwable e) {
                e.printStackTrace();
                NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(), consumer.getSwimlaneId(), "fetch MessageEvent error" + e.getMessage());
                LOGGER.error("fetch MessageEvent error!", e);
            }
        } while (null != events && !events.isEmpty());

        try {
            //退出轮训循环，判断累计查不到数据时间，按照配置发送邮件告警
            LocalDate now = LocalDate.now();
            long nofetchTime = null != lastNoneFetchTime ? Math.abs(Duration.between(now, lastNoneFetchTime).getSeconds()) : -1;
            boolean overThresHold = fetchNoticeThreshould > -1  && nofetchTime >= fetchNoticeThreshould;
            boolean triggerNotice = null == lastNoneFetchNoticeTime
                    || Math.abs(Duration.between(now, lastNoneFetchNoticeTime).getSeconds()) >= fetchNoticeSpan;
            //fetchNoticeThreshould，并且持续fetchNoticeSpan秒没有发送通知
            if (overThresHold && triggerNotice) {
                NodeLog log = new NodeLog(NodeLog.LogType.TASK_WARNING, work.getTaskId(), work.getDataConsumer().getSwimlaneId(),
                        "\"" + work.getDataConsumer().getClientInfo() + "\"持续" + (nofetchTime / 60) + "分未消费到数据，通知间隔"
                                + (fetchNoticeSpan / 60) + "分");
                NodeLog.upload(log, work.getReceivers());
                lastNoneFetchNoticeTime = now;
            }
            lastNoneFetchTime = now;
            NodeContext.INSTANCE.flushConsumerIdle(work.getTaskId(), work.getDataConsumer().getSwimlaneId(), nofetchTime);
        } catch (Throwable e) {

        }
    }

    @Override
    public boolean canStart() {
        return null != consumer && consumer.canStart();
    }

    @Override
    public Pair<String, List<MessageEvent>> output() {
        return carrier.pullByOrder();
    }

    @Override
    public boolean isPoolEmpty() {
        return carrier.size() == 0;
    }
}
