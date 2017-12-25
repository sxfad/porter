package com.suixingpay.datas.node.task.worker;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */


import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
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
import java.util.concurrent.ThreadFactory;

/**
 * 工作
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskWork {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWork.class);
    private final DefaultNamedThreadFactory factory;
    protected final String taskId;
    protected final String topic;
    protected final DataConnector source;
    private final DataConnector target;
    private final DataConnector consumerSource;
    protected final DTaskStat stat;
    private  final Map<StageType, StageJob> JOBS;
    public TaskWork(String taskId, String topic, DataConnector source, DataConnector target, DataConnector dataSource) {
        factory = new DefaultNamedThreadFactory("TaskWork-" + taskId + "-" + topic);
        this.target = target;
        this.source = source;
        this.topic = topic;
        this.taskId = taskId;
        this.consumerSource = dataSource;
        stat = new DTaskStat(taskId, null, topic);
        JOBS = new LinkedHashMap<StageType, StageJob>(){
            {
                put(StageType.SELECT, new SelectJob(consumerSource, factory));
                put(StageType.EXTRACT, new ExtractJob());
                put(StageType.TRANSFORM, new TransformJob());
                put(StageType.LOAD, new LoadJob());
                put(StageType.DB_CHECK, new AlertJob(source, target));
            }
        };
    }

    public void stop() {
        try {
            LOGGER.info("终止执行任务[" + taskId + "-" + topic + "]");
            //终止阶段性工作,需要
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().stop();
            }
            ClusterProvider.sendCommand(new TaskStopCommand(taskId,topic));
        } catch (Exception e) {
            LOGGER.error("终止执行任务[" + taskId + "-" + topic + "]异常", e);
        }
    }

    public void start() {
        try {
            LOGGER.info("开始执行任务[" + taskId + "-" + topic + "]");
            ClusterProvider.sendCommand(new TaskRegisterCommand(taskId, topic));
            //开始阶段性工作
            for (Map.Entry<StageType, StageJob> jobs : JOBS.entrySet()) {
                jobs.getValue().start();
            }
        } catch (Exception e) {
            LOGGER.error("开始执行任务[" + taskId + "-" + topic + "]异常", e);
        }
    }
}
