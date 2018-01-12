/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.worker;

import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.cluster.command.TaskStatCommand;
import com.suixingpay.datas.common.cluster.data.DCallback;
import com.suixingpay.datas.common.cluster.data.DObject;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.common.datasource.MQDataSourceWrapper;
import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.common.task.Task;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.datasource.DataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工人
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 11:22
 */
public class TaskWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    private final int workerSequence;
    private final AtomicBoolean STAT = new AtomicBoolean(false);
    private final AtomicBoolean SOURCE_STAT = new AtomicBoolean(false);
    //负责将任务工作者的状态定时上传
    private final ScheduledExecutorService STAT_WORKER;
    private final Map<String,TaskWork> JOBS;
    private final Map<String,TableMapper> TABLE_MAPPERS;
    private DataSourceWrapper source;
    private DataSourceWrapper target;

    public TaskWorker() {
        STAT_WORKER = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("TaskStat"));
        JOBS = new ConcurrentHashMap<>();
        workerSequence = SEQUENCE.incrementAndGet();
        TABLE_MAPPERS = new ConcurrentHashMap<>();
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
                    for (TaskWork job : JOBS.values()) {
                        job.submitStat();
                    }
                }
            }, 0 ,10000 , TimeUnit.MILLISECONDS);
        } else {
            LOGGER.warn("TaskWorker[] has started already", workerSequence);
        }
    }
    public void stop() {
        if (STAT.compareAndSet(true, false)) {
            LOGGER.info("工人下线.......");
            STAT_WORKER.shutdown();
            for (TaskWork job : JOBS.values()) {
                job.stop();
            }
        } else {
            LOGGER.warn("TaskWorker[] has stoped already", workerSequence);
        }
    }

    public void alloc(Task task) {
        //源数据映射
        if (null != task.getMappers()) {
            for (TableMapper mapper : task.getMappers()) {
                if (mapper.isCustom()) {
                    TABLE_MAPPERS.putIfAbsent(mapper.getUniqueKey(task.getTaskId()), mapper);
                }
            }
        }
        if (SOURCE_STAT.compareAndSet(false, true)) {
            source = DataSourceFactory.INSTANCE.newDataSource(task.getSourceDriver());
            target = DataSourceFactory.INSTANCE.newDataSource(task.getTargetDriver());
            source.create();
            target.create();
        }
        String[]  topics = task.listTopic();
        for (String topic : topics) {
            try {
                //启动JOB
                DataSourceWrapper dataSource = DataSourceFactory.INSTANCE.newDataSource(task.getDataDriver());
                if (dataSource instanceof MQDataSourceWrapper) {
                    MQDataSourceWrapper mqDataSource = (MQDataSourceWrapper) dataSource;
                    mqDataSource.setTopic(topic);
                }
                TaskWork job = new TaskWork(task.getLoader(), task.getTaskId(), topic, source, target, dataSource, this);
                job.start();
                JOBS.put(task.getTaskId() + "_" +topic, job);
            } catch (Exception e){
                LOGGER.error("topic JOB分配出错!", e);
            }
        }
    }

    public Map<String,TableMapper> getTableMapper() {
        return Collections.unmodifiableMap(TABLE_MAPPERS);
    }
}
