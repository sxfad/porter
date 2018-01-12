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
import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.data.DCallback;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.node.core.task.StageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.task.alert.AlertJob;
import com.suixingpay.datas.node.task.extract.ExtractJob;
import com.suixingpay.datas.node.task.load.LoadJob;
import com.suixingpay.datas.node.task.select.SelectJob;
import com.suixingpay.datas.node.task.transform.TransformJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskWork {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWork.class);
    private final String taskId;
    private final String topic;
    private final DataSourceWrapper source;
    private final DataSourceWrapper target;
    private final DataSourceWrapper consumerSource;
    protected final DTaskStat stat;
    private  final Map<StageType, StageJob> JOBS;
    private final String basicThreadName;
    private TableMapper mapper;
    public TaskWork(String taskId, String topic, DataSourceWrapper source, DataSourceWrapper target, DataSourceWrapper dataSource, TaskWorker worker) {
        basicThreadName = "TaskWork-[taskId:" + taskId + "]-[topic:" + topic + "]";
        this.target = target;
        this.source = source;
        this.topic = topic;
        this.taskId = taskId;
        this.consumerSource = dataSource;
        stat = new DTaskStat(taskId, null, topic);

        String mapperKey = taskId + "_" +topic.replace(".", "_");
        this.mapper = worker.getTableMapper().get(mapperKey.toUpperCase());
        if (null == this.mapper) {
            mapperKey = taskId + "__" +topic.split("\\.")[1];
            this.mapper = worker.getTableMapper().get(mapperKey.toUpperCase());
        }
        if (null == this.mapper) {
            mapperKey = taskId + "_" +topic.split("\\.")[0] + "_";
            this.mapper = worker.getTableMapper().get(mapperKey.toUpperCase());
        }
        if (null == this.mapper) {
            mapperKey = taskId + "_" + "_";
            this.mapper = worker.getTableMapper().get(mapperKey.toUpperCase());
        }

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
    }

    public void stop() {
        try {
            LOGGER.info("终止执行任务[{}-{}]", taskId, topic);
            //终止阶段性工作,需要
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().stop();
            }
            //上传消费进度
            submitStat();
            //广播任务结束消息
            ClusterProvider.sendCommand(new TaskStopCommand(taskId,topic));
        } catch (Exception e) {
            LOGGER.error("终止执行任务[{}-{}]异常", taskId, topic, e);
        }
    }

    public void start() {
        try {
            LOGGER.info("开始执行任务[{}-{}]", taskId, topic);
            ClusterProvider.sendCommand(new TaskRegisterCommand(taskId, topic));
            //开始阶段性工作
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().start();
            }
        } catch (Exception e) {
            LOGGER.error("开始执行任务[{}-{}]异常", taskId, topic, e);
        }
    }

    public DataSourceWrapper getSource() {
        return source;
    }

    public DataSourceWrapper getTarget() {
        return target;
    }

    public DataSourceWrapper getConsumerSource() {
        return consumerSource;
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

    public DTaskStat getStat() {
        return stat;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTopic() {
        return topic;
    }

    public TableMapper getMapper() {
        return mapper;
    }


    public void submitStat() {
        try {
            LOGGER.debug("stat before submit:{}", JSON.toJSONString(stat));
            //多线程访问情况下（目前是两个线程:状态上报线程、任务状态更新线程），获取JOB的运行状态。
            DTaskStat newStat = null;
            synchronized (stat) {
                newStat = stat.snapshot(DTaskStat.class);
                LOGGER.debug("stat snapshot:{}", JSON.toJSONString(newStat));
                stat.reset();
                LOGGER.debug("stat after reset:{}", JSON.toJSONString(stat));
                ClusterProvider.sendCommand(new TaskStatCommand(newStat, new DCallback() {
                    @Override
                    public void callback(DObject object) {
                        DTaskStat remoteData = (DTaskStat) object;

                        if(stat.getUpdateStat().compareAndSet(false, true)) {
                            //最后检查点
                            if (null == stat.getLastCheckedTime()) {
                                stat.setLastLoadedTime(remoteData.getLastLoadedTime());
                            }
                            //最初启动时间
                            if (null != remoteData.getStatedTime()) {
                                stat.setStatedTime(remoteData.getStatedTime());
                            }
                        }
                    }
                }));
            }
        } catch (Exception e) {
            LOGGER.error("上传任务消费进度出错", e);
        }
    }
}
