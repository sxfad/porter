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
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.core.util.CallbackMethodCreator;
import com.suixingpay.datas.node.task.worker.TaskWork;

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
    public LoadJob(TaskWork work) {
        super(work.getBasicThreadName());
        this.dataLoder = work.getDataLoader();
        this.work = work;
    }

    @Override
    protected void doStop() {
        try {
            dataLoder.shutdown();
        } catch (InterruptedException e) {
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
            try {
                bucket = work.waitEvent(StageType.TRANSFORM);
                //异常
                if (null != bucket && null != bucket.getException()) {
                    throw new TaskStopTriggerException(bucket.getException());
                }

                //执行载入逻辑
                if (null != bucket && null == bucket.getException()) {
                    boolean loadResult = dataLoder.load(bucket, new CallbackMethodCreator() {
                        @Override
                        public <T> T invokeWithResult(Object... params) {
                            if (null == params || params.length != 2) return null;
                            String schema = String.valueOf(params[0]);
                            String table =  String.valueOf(params[1]);
                            return (T) work.getDTaskStat(schema, table);
                        }
                    });
                    //提交同步点
                    if (null != bucket.getRows() && !bucket.getRows().isEmpty() && loadResult) {
                        String position = bucket.getRows().get(bucket.getRows().size() - 1).getPosition();
                        LOGGER.debug("开始提交消费同步点:{}", position);
                        ClusterProviderProxy.INSTANCE.broadcast(new TaskPositionUploadCommand(work.getTaskId(),
                                work.getDataConsumer().getSwimlaneId(), position));
                        LOGGER.debug("提交消费同步点到集群策略:{}", position);
                        work.getDataConsumer().commitPosition(position);
                        LOGGER.debug("提交消费同步点到消费器客户端:{}", position);
                    } else {
                        throw new TaskStopTriggerException("批次" + bucket.getSequence() + "Load失败!");
                    }
                }
            } catch (TaskStopTriggerException stopException) {
                LOGGER.error("Load ETLRow error", stopException);
                stopException.printStackTrace();
                work.stopAndAlarm(stopException.getMessage());
            } catch (Exception e) {
                NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(), work.getDataConsumer().getSwimlaneId(),
                        "Load ETLRow error"  + e.getMessage());
                LOGGER.error("Load ETLRow error!", e);
            }
        } while (null != bucket);
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
}
