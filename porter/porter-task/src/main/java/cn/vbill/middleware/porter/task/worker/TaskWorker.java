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

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskRegisterCommand;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.task.exception.DataConsumerBuildException;
import cn.vbill.middleware.porter.common.task.exception.DataLoaderBuildException;
import cn.vbill.middleware.porter.common.task.exception.TaskLockException;
import cn.vbill.middleware.porter.common.task.exception.WorkResourceAcquireException;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.common.util.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.entity.Task;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.core.task.entity.TableMapper;
import cn.vbill.middleware.porter.task.TaskController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 工人
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 11:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 11:22
 */
public class TaskWorker {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskWorker.class);
    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    private final int workerSequence;
    private final AtomicBoolean stat = new AtomicBoolean(false);
    //负责将任务工作者的状态定时上传
    private final ScheduledExecutorService workerStatJob;
    private final TaskController controller;

    /**
     * consumeSourceId -> work
     */
    private final Map<String, ArrayBlockingQueue<TaskWork>> jobs;
    private final Map<String, TableMapper> tableMappers;
    private volatile WarningOwner owner;

    public TaskWorker(TaskController controller) {
        workerStatJob = Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("TaskStat"));
        jobs = new ConcurrentHashMap<>();
        workerSequence = SEQUENCE.incrementAndGet();
        tableMappers = new ConcurrentHashMap<>();
        this.controller = controller;
    }

    /**
     * start
     *
     * @date 2018/8/9 下午2:19
     * @param: []
     * @return: void
     */
    public void start() {
        if (stat.compareAndSet(false, true)) {
            LOGGER.info("工人上线.......");
            workerStatJob.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    //每1秒上传一次消费进度
                    for (ArrayBlockingQueue<TaskWork> job : jobs.values()) {
                        job.peek().submitStat();
                    }
                }
            }, 0, 1, TimeUnit.MINUTES);
        } else {
            LOGGER.warn("TaskWorker[{}] has started already", workerSequence);
        }
    }

    /**
     * stop
     *
     * @date 2018/8/9 下午2:19
     * @param: []
     * @return: void
     */
    public void stop() {
        if (stat.compareAndSet(true, false)) {
            LOGGER.info("工人下线.......");
            workerStatJob.shutdownNow();
            for (ArrayBlockingQueue<TaskWork> job : jobs.values()) {
                job.peek().stopWork();
            }
        } else {
            LOGGER.warn("TaskWorker[] has stopped already", workerSequence);
        }
    }

    /**
     * stopJob
     *
     * @date 2018/8/9 下午2:20
     * @param: [swimlaneId]
     * @return: void
     */
    public void stopJob(String... swimlaneId) {
        Arrays.stream(swimlaneId).forEach(c -> {
            if (jobs.containsKey(c)) {
                jobs.get(c).peek().stopWork();
            }
        });
    }


    /**
     * alloc
     *
     * @date 2018/8/9 下午2:20
     * @param: [taskConfig]
     * @return: void
     */
    public void alloc(TaskConfig taskConfig) throws DataConsumerBuildException, DataLoaderBuildException, ConfigParseException, ClientException {
        //抛出从TaskConfig构建Task的异常
        Task task = Task.fromConfig(taskConfig);
        task.getMappers().forEach(m -> {
            tableMappers.putIfAbsent(m.getUniqueKey(task.getTaskId()), m);
        });
        //根据DataConsumer所使用ConsumeClient的消费拆分细则拆分consumer
        for (DataConsumer c : task.getConsumers()) {
            try {
                TaskWork job = new TaskWork(c, task.getLoader(), task.getTaskId(), task.getReceivers(), this, task.getPositionCheckInterval(),
                        task.getAlarmPositionCount());
                register(c.getSwimlaneId(), job);
                job.start();
            } catch (Throwable e) {
                LOGGER.info("work[{}-{}]启动失败", task.getTaskId(), c.getSwimlaneId());
            }
        }
    }

    public Map<String, TableMapper> getTableMapper() {
        return Collections.unmodifiableMap(tableMappers);
    }

    public boolean isNoWork() {
        return jobs.isEmpty();
    }

    private void register(String swimlaneId, TaskWork work) throws InterruptedException, TaskLockException, WorkResourceAcquireException {
        jobs.computeIfAbsent(swimlaneId, key -> new ArrayBlockingQueue<>(1)).put(work);
        //申请work资源
        if (!NodeContext.INSTANCE.acquireWork()) {
            jobs.remove(swimlaneId);
            throw new WorkResourceAcquireException("申请worker资源失败,未申请到可供任务执行的资源");
        }
        try {
            //会抛出分布式锁任务抢占异常
            ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskRegisterCommand(work.getTaskId(), swimlaneId));
        } catch (Throwable e) {
            jobs.remove(swimlaneId);
            NodeContext.INSTANCE.releaseWork();
            throw new TaskLockException(e);
        }
    }

    protected void unregister(String swimlaneId) {
        ArrayBlockingQueue<TaskWork> queue = jobs.remove(swimlaneId);
        if (null != queue) {
            queue.poll();
            NodeContext.INSTANCE.releaseWork();
        }
    }

    protected void  stopWork(String taskId, String...swimlaneId) {
        controller.stopTask(taskId, swimlaneId);
    }

    public void  transmit(WarningOwner owner) {
        this.owner = owner;
        jobs.forEach((k, v) -> {
            TaskWork work = v.peek();
            if (null != work) {
                work.transmit();
            }
        });
    }
    protected final WarningOwner getOwner() {
        return owner;
    }
}
