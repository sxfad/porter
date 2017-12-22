package com.suixingpay.datas.node.task.worker;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */


import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 14:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 14:48
 */
public class TaskJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskJob.class);
    private final ScheduledExecutorService TASK_JOB_ALERT;
    protected final String taskId;
    protected final String topic;
    private final DataConnector source;
    private final DataConnector target;
    private final DataConnector consumerSource;
    protected final DTaskStat stat;
    


    public TaskJob(String taskId, String topic, DataConnector source, DataConnector target, DataConnector dataSource) {
        this.target = target;
        this.source = source;
        this.topic = topic;
        this.taskId = taskId;
        this.consumerSource = dataSource;
        this.TASK_JOB_ALERT = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("TaskJobAlert-" + taskId + "-" + topic));
        stat = new DTaskStat(taskId, null, topic);
    }

    public void stop() {
        try {
            consumerSource.disconnect();
            TASK_JOB_ALERT.shutdown();
            ClusterProvider.sendCommand(new TaskStopCommand(taskId,topic));
        } catch (Exception e) {
            LOGGER.error("终止执行任务Job[" + taskId + "-" + topic + "]异常", e);
        }
    }

    public void start() {
        consumerSource.connect();
        //告警任务
        TASK_JOB_ALERT.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                //执行任务
                //上传进度
            }
        }, 0, 5, TimeUnit.MINUTES);
    }
}
