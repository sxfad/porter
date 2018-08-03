/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.worker;

import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.exception.DataConsumerBuildException;
import cn.vbill.middleware.porter.common.exception.DataLoaderBuildException;
import cn.vbill.middleware.porter.common.exception.TaskLockException;
import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.core.task.Task;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.core.task.TableMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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
    private final ScheduledExecutorService WORKER_STAT_JOB;



    /**
     * consumeSourceId -> work
     */
    private final Map<String, TaskWork> JOBS;
    private final Map<String, TableMapper> TABLE_MAPPERS;

    public TaskWorker() {
        WORKER_STAT_JOB = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("TaskStat"));
        JOBS = new ConcurrentHashMap<>();
        workerSequence = SEQUENCE.incrementAndGet();
        TABLE_MAPPERS = new ConcurrentHashMap<>();
    }

    public void start() {
        if (STAT.compareAndSet(false, true)) {
            LOGGER.info("工人上线.......");
            WORKER_STAT_JOB.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //如果没有JOB，让线程睡眠1分钟.
                    if (JOBS.isEmpty()) {
                        try {
                            Thread.sleep(60000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            
                        }
                    }
                    //每1秒上传一次消费进度
                    for (TaskWork job : JOBS.values()) {
                        job.submitStat();
                    }
                }
            }, 0, 1, TimeUnit.MINUTES);
        } else {
            LOGGER.warn("TaskWorker[] has started already", workerSequence);
        }
    }

    public void stop() {
        if (STAT.compareAndSet(true, false)) {
            LOGGER.info("工人下线.......");
            WORKER_STAT_JOB.shutdown();
            for (TaskWork job : JOBS.values()) {
                job.stop();
            }
        } else {
            LOGGER.warn("TaskWorker[] has stopped already", workerSequence);
        }
    }

    public void stopJob(String... swimlaneId) {
        Arrays.stream(swimlaneId).forEach(c -> {
            if (JOBS.containsKey(c)) {
                JOBS.get(c).stop();
                JOBS.remove(c);
            }
        });
    }

    public void alloc(TaskConfig taskConfig) throws DataConsumerBuildException, DataLoaderBuildException, ConfigParseException, ClientException {
        //抛出从TaskConfig构建Task的异常
        Task task = Task.fromConfig(taskConfig);
        task.getMappers().forEach(m -> {
            TABLE_MAPPERS.putIfAbsent(m.getUniqueKey(task.getTaskId()), m);
        });
        //根据DataConsumer所使用ConsumeClient的消费拆分细则拆分consumer
        task.getConsumers().forEach(c -> {
            TaskWork job = null;
            try {
                //启动JOB
                job = new TaskWork(c, task.getLoader(), task.getTaskId(), task.getReceivers(), this, task.getPositionCheckInterval(),
                task.getAlarmPositionCount());
                job.start();
                JOBS.put(c.getSwimlaneId(), job);
            } catch (Throwable e) {
                if (null != job) job.stop();
                //任务抢占异常不属于报错范畴
                if (!(e instanceof TaskLockException)) {
                    LOGGER.error("Consumer JOB[{}] failed to start!", c.getSwimlaneId(), e);
                    NodeLog.upload(NodeLog.LogType.TASK_LOG, task.getTaskId(), c.getSwimlaneId(), e.getMessage());
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public Map<String, TableMapper> getTableMapper() {
        return Collections.unmodifiableMap(TABLE_MAPPERS);
    }

    public boolean isNoWork() {
        return JOBS.isEmpty();
    }
}
