package com.suixingpay.datas.node.task.worker;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.connector.MQConnector;
import com.suixingpay.datas.common.task.Task;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.connector.ConnectorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 工人
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 11:22
 */
public class TaskWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);
    private final AtomicBoolean STAT = new AtomicBoolean(false);
    private final AtomicBoolean SOURCE_STAT = new AtomicBoolean(false);
    //负责将任务工作者的状态定时上传
    private final ScheduledExecutorService STAT_WORKER;
    private final List<TaskWork> JOBS;

    private DataConnector source;
    private DataConnector target;

    public TaskWorker() {
        STAT_WORKER = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("TaskStat"));
        JOBS = new CopyOnWriteArrayList<>();
    }
    public void start() {
        if (STAT.compareAndSet(false, true)) {
            LOGGER.info("工人上线.......");
            STAT_WORKER.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //如果没有JOB，让线程睡眠1分钟.
                    if (JOBS.isEmpty()) {
                        try {
                            Thread.sleep(60000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //每10秒上传一次消费进度
                    for (TaskWork job : JOBS) {
                        try {
                            //获取JOB的运行状态
                            DTaskStat newStat = job.stat.snapshot(DTaskStat.class);
                            job.stat.reset();
                            ClusterProvider.sendCommand(new TaskStatCommand(newStat));
                        } catch (Exception e) {
                            LOGGER.error("上传任务消费进度出错", e);
                        }
                    }
                }
            }, 0 ,10000 , TimeUnit.MILLISECONDS);
        } else {
            LOGGER.warn("TaskWorker[" + "" + "] has started already");
        }
    }
    public void stop() {
        if (STAT.compareAndSet(true, false)) {
            LOGGER.info("工人下线.......");
            STAT_WORKER.shutdown();
            for (TaskWork job : JOBS) {
                job.stop();
            }
        } else {
            LOGGER.warn("TaskWorker[" + "" + "] has stoped already");
        }
    }

    public void alloc(Task task) {
        if (SOURCE_STAT.compareAndSet(false, true)) {
            source = ConnectorContext.INSTANCE.newConnector(task.getSourceDriver());
            target = ConnectorContext.INSTANCE.newConnector(task.getTargetDriver());
            source.connect();
            target.connect();
        }
        String[]  topics = task.listTopic();
        for (String topic : topics) {
            try {
                //启动JOB
                DataConnector dataSource = ConnectorContext.INSTANCE.newConnector(task.getDataDriver());
                if (dataSource instanceof MQConnector) {
                    MQConnector connector = (MQConnector) dataSource;
                    connector.setTopic(topic);
                }
                TaskWork job = new TaskWork(task.getTaskId(), topic, source, target, dataSource);
                job.start();
                JOBS.add(job);
            } catch (Exception e){
                LOGGER.error("topic JOB分配出错!", e);
            }
        }
    }
}
