/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.worker;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.StatisticUploadCommand;
import com.suixingpay.datas.common.cluster.command.TaskPositionQueryCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.command.TaskStoppedByErrorCommand;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatQueryCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.data.DCallback;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.exception.WorkResourceAcquireException;
import com.suixingpay.datas.common.statistics.NodeLog;
import com.suixingpay.datas.common.statistics.TaskPerformance;
import com.suixingpay.datas.node.core.NodeContext;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.task.StageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.core.task.TableMapper;
import com.suixingpay.datas.node.task.TaskController;
import com.suixingpay.datas.node.task.alert.AlertJob;
import com.suixingpay.datas.node.task.extract.ExtractJob;
import com.suixingpay.datas.node.task.load.LoadJob;
import com.suixingpay.datas.node.task.select.SelectJob;
import com.suixingpay.datas.node.task.transform.TransformJob;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskWork {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWork.class);
    private final AtomicBoolean STAT = new AtomicBoolean(false);

    private final String taskId;

    //单消费泳道
    private final DataConsumer dataConsumer;
    private final DataLoader dataLoader;
    /**
     * stageType -> job
     */
    private  final Map<StageType, StageJob> JOBS;
    private final String basicThreadName;

    /**
     * schema_table -> TaskStat
     */
    private final Map<String, DTaskStat> stats;
    private final Map<String, TableMapper> mappers;
    private final TaskWorker worker;

    private final List<AlertReceiver> receivers;

    /**
     * 触发任务停止标识，生命周期内，仅有一次
     */
    private final AtomicBoolean stopTrigger = new AtomicBoolean(false);

    public TaskWork(DataConsumer dataConsumer, DataLoader dataLoader, String taskId, List<AlertReceiver> receivers,
                    TaskWorker worker, long positionCheckInterval, long alarmPositionCount) throws Exception {
        this.dataConsumer = dataConsumer;
        this.dataLoader = dataLoader;
        basicThreadName = "TaskWork-[taskId:" + taskId + "]-[consumer:" + dataConsumer.getSwimlaneId() + "]";
        this.taskId = taskId;
        this.stats = new ConcurrentHashMap<>();
        this.mappers = new ConcurrentHashMap<>();
        this.worker = worker;
        this.receivers = Collections.unmodifiableList(receivers);
        TaskWork work = this;
        JOBS = new LinkedHashMap<StageType, StageJob>() {
            {
                put(StageType.SELECT, new SelectJob(work));
                put(StageType.EXTRACT, new ExtractJob(work));
                put(StageType.TRANSFORM, new TransformJob(work));
                put(StageType.LOAD, new LoadJob(work, positionCheckInterval, alarmPositionCount));

                /**
                 * 源端数据源支持元数据查询
                 */
                if (dataConsumer.supportMetaQuery()) {
                    put(StageType.DB_CHECK, new AlertJob(work));
                }
            }
        };

        //从集群模块获取任务状态统计信息
        ClusterProviderProxy.INSTANCE.broadcast(new TaskStatQueryCommand(taskId, dataConsumer.getSwimlaneId(), new DCallback() {
            @Override
            public void callback(List<DObject> objects) {
                for (DObject object : objects) {
                    DTaskStat stat = (DTaskStat) object;
                    getDTaskStat(stat.getSchema(), stat.getTable());
                }
            }
        }));
    }

    protected void stop() {
        if (STAT.compareAndSet(true, false)) {
            try {
                LOGGER.info("终止执行任务[{}-{}]", taskId, dataConsumer.getSwimlaneId());
                //终止阶段性工作,需要
                for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                    //确保每个阶段工作都被执行
                    try {
                        LOGGER.info("终止执行工作[{}-{}-{}]", taskId, dataConsumer.getSwimlaneId(), jobs.getValue().getClass().getSimpleName());
                        jobs.getValue().stop();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                try {
                    //上传消费进度
                    submitStat();
                } catch (Exception e) {
                    NodeLog.upload(NodeLog.LogType.TASK_LOG, taskId, dataConsumer.getSwimlaneId(), "停止上传消费进度失败:" + e.getMessage());
                }
                try {
                    //广播任务结束消息
                    ClusterProviderProxy.INSTANCE.broadcast(new TaskStopCommand(taskId, dataConsumer.getSwimlaneId()));
                } catch (Exception e) {
                    NodeLog.upload(NodeLog.LogType.TASK_LOG, taskId, dataConsumer.getSwimlaneId(), "广播TaskStopCommand失败:" + e.getMessage());
                }
            } catch (Exception e) {
                e.printStackTrace();
                NodeLog.upload(NodeLog.LogType.TASK_LOG, taskId, dataConsumer.getSwimlaneId(), "任务关闭失败:" + e.getMessage());
                LOGGER.error("终止执行任务[{}-{}]异常", taskId, dataConsumer.getSwimlaneId(), e);
            } finally {
                NodeContext.INSTANCE.releaseWork();
            }
        }
    }

    protected void start() throws Exception {
        if (STAT.compareAndSet(false, true)) {
            LOGGER.info("开始执行任务[{}-{}]", taskId, dataConsumer.getSwimlaneId());
            //申请work资源
            if (!NodeContext.INSTANCE.acquireWork()) {
                throw new WorkResourceAcquireException("未申请到可供任务执行的资源");
            }


            //会抛出分布式锁任务抢占异常
            ClusterProviderProxy.INSTANCE.broadcast(new TaskRegisterCommand(taskId, dataConsumer.getSwimlaneId()));
            //开始阶段性工作
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().start();
            }

            LOGGER.info("开始获取任务消费泳道[{}-{}]上次同步点", taskId, dataConsumer.getSwimlaneId());
            //获取上次任务进度
            ClusterProviderProxy.INSTANCE.broadcast(new TaskPositionQueryCommand(taskId, dataConsumer.getSwimlaneId(), new DCallback() {
                @Override
                @SneakyThrows(TaskStopTriggerException.class)
                public void callback(String position) {
                    LOGGER.info("获取任务消费泳道[{}-{}]上次同步点->{}，通知SelectJob", taskId, dataConsumer.getSwimlaneId(), position);
                    dataConsumer.initializePosition(taskId, dataConsumer.getSwimlaneId(), position);
                }
            }));
        }
    }


    public String getBasicThreadName() {
        return basicThreadName;
    }

    public <T> T waitEvent(StageType type) throws Exception {
        return JOBS.get(type).output();
    }
    public <T> T waitSequence() {
        return ((ExtractJob) JOBS.get(StageType.EXTRACT)).getNextSequence();
    }

    public boolean isPoolEmpty(StageType type) {
        return JOBS.get(type).isPoolEmpty();
    }


    public String getTaskId() {
        return taskId;
    }

    public void submitStat() {
        stats.forEach((s, stat) -> {
            LOGGER.debug("stat before submit:{}", JSON.toJSONString(stat));
            //多线程访问情况下（目前是两个线程:状态上报线程、任务状态更新线程），获取JOB的运行状态。
            DTaskStat newStat = null;
            synchronized (stat) {
                newStat = stat.snapshot(DTaskStat.class);
                LOGGER.debug("stat snapshot:{}", JSON.toJSONString(newStat));
                stat.reset();
                try {
                    ClusterProviderProxy.INSTANCE.broadcast(new TaskStatCommand(newStat, new DCallback() {
                        @Override
                        public void callback(DObject object) {
                            DTaskStat remoteData = (DTaskStat) object;
                            if (stat.getUpdateStat().compareAndSet(false, true)) {
                                //最后检查点
                                if (null == stat.getLastCheckedTime()) {
                                    stat.setLastLoadedDataTime(remoteData.getLastLoadedDataTime());
                                }
                                //最初启动时间
                                if (null != remoteData.getRegisteredTime()) {
                                    stat.setRegisteredTime(remoteData.getRegisteredTime());
                                }
                            }
                        }
                    }));
                } catch (Throwable e) {
                    NodeLog.upload(NodeLog.LogType.TASK_LOG, taskId, dataConsumer.getSwimlaneId(), "上传任务状态信息失败:" + e.getMessage());
                }

                //上传统计
                try {
                    //TaskPerformance
                    if (NodeContext.INSTANCE.isUploadStatistic()) {
                        ClusterProviderProxy.INSTANCE.broadcast(new StatisticUploadCommand(new TaskPerformance(newStat)));
                    }
                } catch (Throwable e) {
                    NodeLog.upload(NodeLog.LogType.TASK_LOG, taskId, dataConsumer.getSwimlaneId(), "上传任务统计信息失败:" + e.getMessage());
                }
            }
        });
    }



    public TableMapper getTableMapper(String schema, String table) {
        String key = schema + "." + table;
        TableMapper mapper = mappers.computeIfAbsent(key, s -> {
            TableMapper tmp = null;
            String mapperKey = taskId + "_" + schema + "_" + table;
            tmp = worker.getTableMapper().get(mapperKey);
            if (null == tmp) {
                mapperKey = taskId + "__" + table;
                tmp = worker.getTableMapper().get(mapperKey);
            }
            if (null == tmp) {
                mapperKey = taskId + "_" + schema + "_";
                tmp = worker.getTableMapper().get(mapperKey);
            }
            if (null == tmp) {
                mapperKey = taskId + "_" + "_";
                tmp = worker.getTableMapper().get(mapperKey);
            }
            return tmp;
        });
        return mapper;
    }

    public DTaskStat getDTaskStat(String schema, String table) {
        String key = schema + "." + table;
        DTaskStat stat = stats.computeIfAbsent(key, s ->
                new DTaskStat(taskId, null, dataConsumer.getSwimlaneId(), schema, table)
        );
        return stat;
    }

    public List<DTaskStat>  getStats() {
        return Collections.unmodifiableList(stats.values().stream().collect(Collectors.toList()));
    }

    public void stopAndAlarm(final String notice) {
        if (stopTrigger.compareAndSet(false, true)) {
            new Thread("suixingpay-TaskStopByErrorTrigger-stopTask-" + taskId + "-" + dataConsumer.getSwimlaneId()) {
                @Override
                public void run() {
                    StringBuffer alarmNoticeBuilder = new StringBuffer(notice);
                    String alarmNotice = notice;
                    //增加插件连接信息
                    try {
                        alarmNoticeBuilder.append(System.lineSeparator()).append("消费源:").append(dataConsumer.getClientInfo()).append(System.lineSeparator())
                                .append("目标端:").append(dataLoader.getClientInfo());
                        alarmNotice = alarmNoticeBuilder.toString();
                    } catch (Throwable e) {

                    }
                    try {
                        ClusterProviderProxy.INSTANCE.broadcast(new TaskStoppedByErrorCommand(taskId, dataConsumer.getSwimlaneId(), alarmNotice));
                    } catch (Throwable e) {
                        LOGGER.error("在集群策略存储引擎标识任务因错误失败出错:{}", e.getMessage());
                        e.printStackTrace();
                    }
                    try {
                        //停止任务
                        NodeContext.INSTANCE.getBean(TaskController.class).stopTask(taskId, dataConsumer.getSwimlaneId());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        //调整节点健康级别
                        NodeContext.INSTANCE.markTaskError(taskId, alarmNotice);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        LOGGER.info("开始发送日志通知.....");
                        //上传日志
                        NodeLog.upload(NodeLog.LogType.TASK_ALARM, taskId, dataConsumer.getSwimlaneId(), alarmNotice, getReceivers());
                        LOGGER.info("结束发送日志通知.....");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }

    public DataConsumer getDataConsumer() {
        return dataConsumer;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }

    public List<AlertReceiver> getReceivers() {
        return receivers;
    }


    /**
     * 当前任务是否触发
     */
    public boolean triggerStopped() {
        return stopTrigger.get();
    }
}
