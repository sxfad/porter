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
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatQueryCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.data.DCallback;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.task.StageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.core.task.TableMapper;
import com.suixingpay.datas.node.task.alert.AlertJob;
import com.suixingpay.datas.node.task.extract.ExtractJob;
import com.suixingpay.datas.node.task.load.LoadJob;
import com.suixingpay.datas.node.task.select.SelectJob;
import com.suixingpay.datas.node.task.transform.TransformJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskWork {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWork.class);
    private final String taskId;

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


    public TaskWork(DataConsumer dataConsumer, DataLoader dataLoader, String taskId, TaskWorker worker) throws Exception {
        this.dataConsumer = dataConsumer;
        this.dataLoader = dataLoader;
        basicThreadName = "TaskWork-[taskId:" + taskId + "]-[consumer:" + dataConsumer.getSourceId() + "]";
        this.taskId = taskId;
        this.stats = new ConcurrentHashMap<>();
        this.mappers = new ConcurrentHashMap<>();
        this.worker = worker;
        TaskWork work = this;
        JOBS = new LinkedHashMap<StageType, StageJob>(){
            {
                put(StageType.SELECT, new SelectJob(work));
                put(StageType.EXTRACT, new ExtractJob(work));
                put(StageType.TRANSFORM, new TransformJob(work));
                put(StageType.LOAD, new LoadJob(work));
                put(StageType.DB_CHECK, new AlertJob(work));
            }
        };

        //从集群模块获取任务状态统计信息
        ClusterProviderProxy.INSTANCE.broadcast(new TaskStatQueryCommand(taskId, dataConsumer.getSourceId(), new DCallback() {
            @Override
            public void callback(List<DObject> objects) {
                for ( DObject object : objects) {
                    DTaskStat stat = (DTaskStat) object;
                    getDTaskStat(stat.getSchema(), stat.getTable());
                }
            }
        }));
    }

    public void stop() {
        try {
            LOGGER.info("终止执行任务[{}-{}]", taskId, dataConsumer.getSourceId());
            //终止阶段性工作,需要
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().stop();
            }
            //上传消费进度
            submitStat();
            //广播任务结束消息
            ClusterProviderProxy.INSTANCE.broadcast(new TaskStopCommand(taskId,dataConsumer.getSourceId()));
        } catch (Exception e) {
            LOGGER.error("终止执行任务[{}-{}]异常", taskId, dataConsumer.getSourceId(), e);
        }
    }

    public void start() throws Exception {
        LOGGER.info("开始执行任务[{}-{}]", taskId, dataConsumer.getSourceId());
        //会抛出分布式锁任务抢占异常
        ClusterProviderProxy.INSTANCE.broadcast(new TaskRegisterCommand(taskId, dataConsumer.getSourceId()));
        //开始阶段性工作
        for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
            jobs.getValue().start();
        }
    }

    public String getBasicThreadName() {
        return basicThreadName;
    }

    public <T> T waitEvent(StageType type) throws Exception {
        return JOBS.get(type).output();
    }
    public <T> T waitSequence() {
        return ((ExtractJob)JOBS.get(StageType.EXTRACT)).getNextSequence();
    }

    public boolean isPoolEmpty(StageType type) {
        return JOBS.get(type).isPoolEmpty();
    }


    public String getTaskId() {
        return taskId;
    }

    public void submitStat() {
        stats.forEach(new BiConsumer<String, DTaskStat>() {
            @Override
            public void accept(String s, DTaskStat stat) {
                try {
                    LOGGER.debug("stat before submit:{}", JSON.toJSONString(stat));
                    //多线程访问情况下（目前是两个线程:状态上报线程、任务状态更新线程），获取JOB的运行状态。
                    DTaskStat newStat = null;
                    synchronized (stat) {
                        newStat = stat.snapshot(DTaskStat.class);
                        LOGGER.debug("stat snapshot:{}", JSON.toJSONString(newStat));
                        stat.reset();
                        LOGGER.debug("stat after reset:{}", JSON.toJSONString(stat));
                        ClusterProviderProxy.INSTANCE.broadcast(new TaskStatCommand(newStat, new DCallback() {
                            @Override
                            public void callback(DObject object) {
                                DTaskStat remoteData = (DTaskStat) object;
                                if(stat.getUpdateStat().compareAndSet(false, true)) {
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
                        //上传统计
                        //TaskPerformance
                    }
                } catch (Exception e) {
                    LOGGER.error("上传任务消费进度出错", e);
                }
            }
        });
    }

    public DTaskStat getDTaskStat(String schema, String table) {
        String key = schema + "." + table;
        DTaskStat stat = stats.computeIfAbsent(key, new Function<String, DTaskStat>() {
            @Override
            public DTaskStat apply(String s) {
                DTaskStat tmp = new DTaskStat(taskId, null, dataConsumer.getSourceId(), schema, table);
                return tmp;
            }
        });
        return stat;
    }

    public TableMapper getTableMapper(String schema, String table) {
        String key = schema + "." + table;
        TableMapper mapper = mappers.computeIfAbsent(key, new Function<String, TableMapper>() {
            @Override
            public TableMapper apply(String s) {
                TableMapper tmp = null;
                String mapperKey = taskId + "_" + schema + "_" + table;
                tmp = worker.getTableMapper().get(mapperKey.toUpperCase());
                if (null == tmp) {
                    mapperKey = taskId + "__" + table;
                    tmp = worker.getTableMapper().get(mapperKey.toUpperCase());
                }
                if (null == tmp) {
                    mapperKey = taskId + "_" + schema + "_";
                    tmp = worker.getTableMapper().get(mapperKey.toUpperCase());
                }
                if (null == tmp) {
                    mapperKey = taskId + "_" + "_";
                    tmp = worker.getTableMapper().get(mapperKey.toUpperCase());
                }
                return tmp;
            }
        });
        return mapper;
    }

    public List<DTaskStat>  getStats() {
        return Collections.unmodifiableList(stats.values().stream().collect(Collectors.toList()));
    }

    public DataConsumer getDataConsumer() {
        return dataConsumer;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }
}
