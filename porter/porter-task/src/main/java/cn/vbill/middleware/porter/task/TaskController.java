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

package cn.vbill.middleware.porter.task;

import cn.vbill.middleware.porter.common.cluster.event.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.task.event.*;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.TaskContext;
import cn.vbill.middleware.porter.core.task.entity.Task;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.task.worker.TaskWorker;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 监工
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 20:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 20:53
 */

@Component
@Scope("singleton")
public class TaskController implements TaskEventListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskController.class);
    private final AtomicBoolean stat = new AtomicBoolean(false);
    /**
     * taskId -> worker
     */
    private final Map<String, TaskWorker> workerMap = new ConcurrentHashMap<>();
    private final Lock workerLock = new ReentrantLock();

    /**
     * start
     *
     * @date 2018/8/9 下午1:59
     * @param: []
     * @return: void
     */
    public void start() {
        start(null);
    }

    /**
     * start
     *
     * @date 2018/8/9 下午2:00
     * @param: [initTasks]
     * @return: void
     */
    public void start(List<TaskConfig> initTasks) {
        try {
            if (stat.compareAndSet(false, true)) {
                //从配置中心监听任务变更事件，进行任务创建关闭等操作
                ClusterProviderProxy.INSTANCE.addTaskListener(this);
                //初始化本地任务
                if (null != initTasks && !initTasks.isEmpty()) {
                    for (TaskConfig t : initTasks) {
                        //初始化配置文件任务为本地任务
                        t.setLocalTask(true);
                        t.setNodeId(NodeContext.INSTANCE.getNodeId());
                        /**
                         * 2018-10-19 12:00:00
                         * 除了单机模式外(standalone)，本地任务只上传不启动
                         *
                         */
                        if (NodeContext.INSTANCE.getWorkMode() == ClusterPlugin.STANDALONE) {
                            startTask(t);
                        } else {
                            registerLocalTask(t);
                        }
                    }
                }

            } else {
                LOGGER.warn("Task controller has started already");
            }
        } finally {
            //类unix操作系统
            if (!SystemUtils.IS_OS_WINDOWS) {
                //进程退出钩子
                //因为JVM不能保证ShutdownHook一定能执行，通过自定义信号实现优雅下线。
                Signal graceShutdown = new Signal("USR2");
                //不同的操作系统USR2信号量数值不一样，不支持windows操作系统
                LOGGER.info("Shutdown gracefully with signal {}. [kill -{} {}]", graceShutdown.getName(), graceShutdown.getNumber(),
                        MachineUtils.getPID());
                Signal.handle(graceShutdown, new SignalHandler() {
                    @Override
                    public void handle(Signal signal) {
                        shutdownHook(true);
                    }
                });
            }

            Runtime.getRuntime().addShutdownHook(new Thread("suixingpay-task-shutdownHook") {
                @Override
                public void run() {
                    shutdownHook(false);
                }
            });
        }
    }

    @Override
    public void onEvent(TaskEvent taskEvent) {
        switch (taskEvent.getType()) {
            case TASK_CONFIG:
                TaskConfig event = ((TaskConfigEvent) taskEvent).getConfig();
                if (event.getStatus().isWorking() && NodeContext.INSTANCE.getNodeStatus().isWorking()) {
                    //新建任务如果指定了节点ID,但与当前节点不符时，停止抢占任务
                    if (!StringUtils.isBlank(event.getNodeId())
                            && !("," + event.getNodeId() + ",").contains(("," + NodeContext.INSTANCE.getNodeId() + ","))) {
                        return;
                    }
                    try {
                        startTask(event);
                    } catch (Exception e) {
                        LOGGER.error("启动任务出错!", e);
                    }
                } else if (event.getStatus().isStopped()) {
                    try {
                        stopTask(Task.fromConfig(event, false));
                    } catch (Exception e) {
                        LOGGER.error("停止任务出错!", e);
                    }
                }
                break;
            case TASK_OWNER:
                TaskOwnerEvent taskOwner = (TaskOwnerEvent) taskEvent;
                TaskWorker worker = workerMap.get(taskOwner.getTaskId());
                if (null != worker) {
                    worker.transmit(taskOwner.getOwner());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 停止任务
     *
     * @date 2018/8/9 下午2:00
     * @param: [taskId, swimlaneId]
     * @return: void
     */
    public void stopTask(String taskId, String... swimlaneId) {
        workerLock.lock();
        TaskWorker worker = workerMap.get(taskId);
        if (null != worker) {
            worker.stopJob(swimlaneId);
            if (worker.isNoWork()) workerMap.remove(taskId).stop();
        }
        workerLock.unlock();
    }

    /**
     * 开启任务
     *
     * @date 2018/8/9 下午2:00
     * @param: [task]
     * @return: void
     */
    private void startTask(TaskConfig task) {
        workerLock.lock();
        TaskWorker worker = workerMap.computeIfAbsent(task.getTaskId(), s -> new TaskWorker(this));
        //尝试通过ClusterProvider的分布式锁功能锁定资源。
        try {
            worker.alloc(task);
            worker.start();
        } catch (Throwable e) {
            TaskContext.warning(new NodeLog(NodeLog.LogType.ERROR, e.getMessage()).bindTaskId(task.getTaskId()).upload());
            LOGGER.error("failed to start task:{}", JSONObject.toJSONString(task), e);
        }
        //考虑到任务启动失败造成的worker闲置(内存、线程),检查worker是否有工作，如果空闲，释放worker资源
        if (worker.isNoWork()) {
            workerMap.remove(task.getTaskId()).stop();
        }
        workerLock.unlock();
    }

    /**
     * stop
     *
     * @date 2018/8/9 下午2:00
     * @param: []
     * @return: boolean
     */
    private boolean stop() {
        if (stat.compareAndSet(true, false)) {
            LOGGER.info("unload task container.......");
            List<TaskWork> works = new ArrayList<>();
            /**
             * 任务停止的方式是给任务发送停止信号,stop方法调用返回并不代表任务已经停止
             * TaskWork本质上是一个线程，线程DEAD是判断任务停止的条件
             * 张科伟 2019-07-01
             */
            workerMap.values().stream().forEach(worker -> works.addAll(worker.stop()));
            LOGGER.info("need stop tasks:{}", works.size());
            while (works.stream().parallel().filter(c -> c.isAlive()).count() > 0) {
                try {
                    LOGGER.info("need stop tasks:{}", works.stream().parallel().filter(c -> c.isAlive()).count());
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    break;
                }
            }
            LOGGER.info("need stop tasks:{}", works.stream().parallel().filter(c -> c.isAlive()).count());
            return true;
        } else {
            LOGGER.warn("task container has already unloaded.");
            return false;
        }
    }

    /**
     * 停止任务
     *
     * @date 2018/8/9 下午2:01
     * @param: [task]
     * @return: void
     */
    private void stopTask(Task task) {
        List<String> swimlaneIdList = task.getConsumers().stream().map(DataConsumer::getSwimlaneId).collect(Collectors.toList());
        stopTask(task.getTaskId(), swimlaneIdList.toArray(new String[0]));
    }

    /**
     * shutdownHook
     *
     * @date 2018/8/9 下午2:01
     * @param: [exit]
     * @return: void
     */
    private void shutdownHook(boolean exit) {
        //先将节点设置为暂停,避免停止任务后再度消费
        NodeContext.INSTANCE.syncNodeStatus(NodeStatusType.SUSPEND);
        if (this.stop()) {
            //退出群聊需在业务代码执行之后才能执行
            LOGGER.info("退出群聊.......");
            ClusterProviderProxy.INSTANCE.stop();
            if (exit) {
                System.exit(-1);
            }
        }
    }

    private void registerLocalTask(TaskConfig taskConfig) {
        try {
            if (taskConfig.isLocalTask()) {
                ClusterProviderProxy.INSTANCE.broadcastEvent(new TaskPushCommand(taskConfig));
            }
        } catch (Throwable e) {
            LOGGER.warn("注册本地任务到集群失败:{}", taskConfig.getTaskId(), e);
        }
    }
}