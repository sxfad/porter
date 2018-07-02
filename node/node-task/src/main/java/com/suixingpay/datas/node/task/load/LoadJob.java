/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.load;

import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.TaskPositionUploadCommand;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.loader.SubmitStatObject;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 完成SQL事件的最终执行，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:19
 */
public class LoadJob extends AbstractStageJob {
    private final DataLoader dataLoder;
    private final TaskWork work;
    //最新的消费进度差值
    private volatile long newestPositionDiffer = 0;
    private final ScheduledExecutorService positionCheckService;

    public LoadJob(TaskWork work, long positionCheckInterval, long alarmPositionCount) {
        super(work.getBasicThreadName(), 50L);
        this.dataLoder = work.getDataLoader();
        this.work = work;
        //消费进度告警
        if (positionCheckInterval > 0) {
            positionCheckService = Executors.newSingleThreadScheduledExecutor(
                    new DefaultNamedThreadFactory(work.getBasicThreadName() + "-positionConsumedCheck"));
            positionCheckService.scheduleAtFixedRate(() -> {
                //当前进度差值超过告警线
                if (newestPositionDiffer >= alarmPositionCount) {
                    String taskId = work.getTaskId();
                    String swimlaneId = work.getDataConsumer().getSwimlaneId();
                    NodeLog noticeMsg = new NodeLog(NodeLog.LogType.TASK_WARNING, taskId, swimlaneId,
                            "未消费消息堆积:" + newestPositionDiffer + "条,告警阀值:" + alarmPositionCount);
                    noticeMsg.setTitle("【关注】" + taskId + "-" + swimlaneId + "消息堆积" + newestPositionDiffer + "条");
                    NodeLog.upload(noticeMsg, work.getReceivers());
                }
            }, positionCheckInterval, positionCheckInterval, TimeUnit.SECONDS);
        } else {
            positionCheckService = null;
        }
    }

    @Override
    protected void doStop() {
        try {
            if (null != positionCheckService) positionCheckService.shutdownNow();
            dataLoder.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStart() throws Exception {
        dataLoder.startup();
    }

    @Override
    protected void loopLogic() {
        //只要队列有消息，持续读取
        ETLBucket bucket = null;
        do {
            //确保任务出错停止后不执行do{}逻辑
            if (work.triggerStopped()) break;
            //正常逻辑
            try {
                bucket = work.waitEvent(StageType.TRANSFORM);
                //异常
                if (null != bucket && null != bucket.getException()) {
                    throw new TaskStopTriggerException(bucket.getException());
                }

                //没有异常
                if (null != bucket && null == bucket.getException()) {
                    //执行载入逻辑
                    Pair<Boolean, List<SubmitStatObject>> loadResult = dataLoder.load(bucket);
                    //逻辑执行失败
                    if (!loadResult.getLeft()) throw new TaskStopTriggerException("批次" + bucket.getSequence() + "Load失败!");
                    //提交批次消费同步点
                    if (null != bucket.getPosition()) {
                        LOGGER.debug("提交消费同步点到集群策略:{}", bucket.getPosition().render());
                        newestPositionDiffer = work.getDataConsumer().commitPosition(bucket.getPosition());
                        LOGGER.debug("提交消费同步点到消费器客户端:{}", bucket.getPosition().render());
                        if (bucket.getPosition().checksum()) {
                            LOGGER.info("提交消费同步点:{},消息堆积:{}", bucket.getPosition().render(), newestPositionDiffer);
                            ClusterProviderProxy.INSTANCE.broadcast(new TaskPositionUploadCommand(work.getTaskId(),
                                    work.getDataConsumer().getSwimlaneId(), bucket.getPosition().render()));
                            //LOGGER.info("结束提交消费同步点:{},消息堆积:{}", bucket.getPosition().render(), newestPositionDiffer);
                        }

                        NodeContext.INSTANCE.flushConsumeProcess(
                                work.getTaskId() + "-" + work.getDataConsumer().getSwimlaneId(),
                                newestPositionDiffer + "");
                    }
                    //更新消费统计数据
                    loadResult.getRight().forEach(o -> updateStat(o));
                    //标记数据已清除
                    loadResult.getRight().clear();
                    bucket.markUnUsed();
                }
            } catch (TaskStopTriggerException stopException) {
                LOGGER.error("Load ETLRow error", stopException);
                stopException.printStackTrace();
                work.stopAndAlarm(stopException.getMessage());
                /**
                 * 立即停止目标端载入逻辑,理论上存在任务停止线程和当前载入线程同时执行的情况
                 */
                break;
            } catch (Throwable e) {
                e.printStackTrace();
                NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(), work.getDataConsumer().getSwimlaneId(),
                        "Load ETLRow error"  + e.getMessage());
                LOGGER.error("Load ETLRow error!", e);
            }
        } while (null != bucket && !work.triggerStopped()); //数据不为空并且当前任务没有触发停止告警
    }
    @Override
    public ETLBucket output() throws Exception {
        throw new Exception("unsupported Method");
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.TRANSFORM);
    }

    @Override
    public boolean stopWaiting() {
        return work.getDataConsumer().isAutoCommitPosition();
    }


    /**
     * 更新任务状态
     *  For a prepared statement batch, it is not possible to know the number of rows affected in the database
     *  by each individual statement in the batch.Therefore, all array elements have a value of -2.
     *  According to the JDBC 2.0 specification, a value of -2 indicates that the operation was successful
     *  but the number of rows affected is unknown.
     * @param object
     */
    private void updateStat(SubmitStatObject object) {
        //虽然每个状态值的变更都有stat对象锁，但在最外层加对象锁避免了多次请求的问题（锁可重入），同时保证状态各字段变更一致性
        DTaskStat stat = work.getDTaskStat(object.getSchema(), object.getTable());
        synchronized (stat) {
            int affect = object.getAffect();
            boolean hit = affect > 0 || affect == -2;
            EventType eventType = object.getType();
            switch (eventType.getIndex()) {
                case EventType.DELETE_INDEX:
                    if (hit) {
                        stat.incrementDeleteRow();
                    } else {
                        stat.incrementErrorDeleteRow();
                    }
                    break;
                case EventType.UPDATE_INDEX:
                    if (hit) {
                        stat.incrementUpdateRow();
                    } else {
                        stat.incrementErrorUpdateRow();
                    }
                    break;
                case EventType.INSERT_INDEX:
                    if (hit) {
                        stat.incrementInsertRow();
                    } else {
                        stat.incrementErrorInsertRow();
                    }
                    break;
                case EventType.TRUNCATE_INDEX:
                    if (hit) {
                        stat.incrementDeleteRow();
                    } else {
                        stat.incrementErrorDeleteRow();
                    }
                    break;
            }

            //更新最后执行消息事件的产生时间，用于计算从消息产生到加载如路时间、计算数据同步检查时间
            if (null != object.getOpTime()) stat.setLastLoadedDataTime(object.getOpTime());
            stat.setLastLoadedSystemTime(new Date());
            if (null != object.getPosition()) {
                stat.setProgress(object.getPosition().render());
            }

            //打印当前消息所在点位，方便问题查找
            if (!hit) {
                LOGGER.error("{}.{} {} {}", object.getSchema(), object.getTable(), object.getType().getCode(), object.getPosition().render());
            }
        }
    }
}
