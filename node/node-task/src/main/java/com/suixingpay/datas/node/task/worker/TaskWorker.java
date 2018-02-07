/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.worker;

import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.task.TableMapper;
import com.suixingpay.datas.node.core.task.Task;
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
    //负责将任务工作者的状态定时上传
    private final ScheduledExecutorService STAT_WORKER;
    private final Map<String,TaskWork> JOBS;
    private final Map<String,TableMapper> TABLE_MAPPERS;

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

    public void alloc(TaskConfig taskConfig) throws IllegalAccessException, ClientException, InstantiationException {
        Task task = Task.fromConfig(taskConfig);
        task.getMappers().forEach(m -> {
            TABLE_MAPPERS.putIfAbsent(m.getUniqueKey(task.getTaskId()), m);
        });

        task.getConsumers().forEach(c -> {
            try {
                //启动JOB
                TaskWork job = new TaskWork(c, task.getLoader(), task.getTaskId(), this);
                job.start();
                JOBS.put(task.getTaskId() + "_" + c.getId(), job);
            } catch (Exception e){
                LOGGER.error("topic JOB分配出错!", e);
            }
        });
    }

    public Map<String,TableMapper> getTableMapper() {
        return Collections.unmodifiableMap(TABLE_MAPPERS);
    }
}
