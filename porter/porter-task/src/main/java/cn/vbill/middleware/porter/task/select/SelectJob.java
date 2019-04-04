/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.select;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import cn.vbill.middleware.porter.core.task.job.AbstractStageJob;
import cn.vbill.middleware.porter.datacarrier.DataCarrier;
import cn.vbill.middleware.porter.datacarrier.DataCarrierFactory;
import cn.vbill.middleware.porter.core.task.TaskContext;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 完成SQL事件从数据源的消费。为了提升消费能力，不做过多业务逻辑处理和数据模型转换
 * 获取事件消息，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:15
 */
public class SelectJob extends AbstractStageJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(SelectJob.class);

    //已无效,实际根据任务配置设置单次处理数据数量
    private static final int PULL_BATCH_SIZE = 100;

    //只需要保证select出来的数据不致使ExtractJob的所有消费线程饥饿即可
    private static final int BUFFER_SIZE = 100;

    private final DataConsumer consumer;
    private final TaskWork work;
    private final DataCarrier<List<MessageEvent>> carrier;
    //最后一次查不到数据时间
    private volatile Date lastNoneFetchTime;
    private volatile Date lastNoneFetchNoticeTime;
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
    protected void threadTraceLogic() {
        TaskContext.trace(work.getTaskId(), work.getDataConsumer(), work.getDataLoader(), work.getReceivers());
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
                TaskContext.warning(NodeLog.upload(NodeLog.LogType.INFO, work.getTaskId(), consumer.getSwimlaneId(), "fetch MessageEvent error" + e.getMessage()));
                LOGGER.error("fetch MessageEvent error!", e);
            }
        } while (null != events && !events.isEmpty() && getWorkingStat());

        try {
            //退出轮训循环，判断累计查不到数据时间，按照配置发送邮件告警
            String taskId = work.getTaskId();
            String swimlaneId = work.getDataConsumer().getSwimlaneId();
            Date now = Calendar.getInstance().getTime();
            long nofetchTime = null != lastNoneFetchTime
                    ? TimeUnit.SECONDS.convert(Math.abs(now.getTime() - lastNoneFetchTime.getTime()), TimeUnit.MILLISECONDS) : -1;
            boolean overThresHold = fetchNoticeThreshould > -1  && nofetchTime >= fetchNoticeThreshould;
            boolean triggerNotice = null == lastNoneFetchNoticeTime
                    || TimeUnit.SECONDS.convert(Math.abs(now.getTime() - lastNoneFetchNoticeTime.getTime()), TimeUnit.MILLISECONDS) >= fetchNoticeSpan;
            //fetchNoticeThreshould，并且持续fetchNoticeSpan秒没有发送通知
            if (overThresHold && triggerNotice) {
                TaskContext.warning(new NodeLog(NodeLog.LogType.WARNING, TaskContext.trace().getTaskId(), TaskContext.trace().getSwimlaneId(),
                        "\"" + work.getDataConsumer().getClientInfo() + "\"已持续" + (nofetchTime / 60) + "分钟未消费到数据，通知间隔"
                                + (fetchNoticeSpan / 60) + "分钟").bindTitle("【关注】" + taskId + "-" + swimlaneId + "持续无数据消费" + (nofetchTime / 60) + "分钟"));
                lastNoneFetchNoticeTime = now;
            }
            if (null == lastNoneFetchTime) {
                lastNoneFetchTime = now;
            }
            NodeContext.INSTANCE.flushConsumerIdle(taskId, swimlaneId, nofetchTime);
        } catch (Throwable e) {
            e.printStackTrace();
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
