/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 20:53
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.task;

import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.core.task.Task;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.core.consumer.DataConsumer;
import cn.vbill.middleware.porter.task.worker.TaskWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import sun.misc.Signal;
import sun.misc.SignalHandler;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
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
    private final Map<String, TaskWorker> WORKER_MAP = new ConcurrentHashMap<>();

    public void start() throws IllegalAccessException, ClientException, InstantiationException {
        start(null);
    }

    public void start(List<TaskConfig> initTasks) {
        try {
            if (stat.compareAndSet(false, true)) {
                if (null != initTasks && !initTasks.isEmpty()) {
                    for (TaskConfig t : initTasks) {
                        startTask(t);
                    }
                }

                //从配置中心监听任务变更事件，进行任务创建关闭等操作
                ClusterProviderProxy.INSTANCE.addTaskListener(this);
            } else {
                LOGGER.warn("Task controller has started already");
            }
        } finally {
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

            Runtime.getRuntime().addShutdownHook(new Thread("suixingpay-task-shutdownHook") {
                @Override
                public void run() {
                    shutdownHook(false);
                }
            });
        }
    }

    @Override
    public void onEvent(TaskConfig event) {
        if (event.getStatus().isWorking() && NodeContext.INSTANCE.getNodeStatus().isWorking()) {
            //新建任务如果指定了节点ID,但与当前节点不符时，停止抢占任务
            if (!StringUtils.isBlank(event.getNodeId())
                    && !("," + event.getNodeId() + ",").contains(("," + NodeContext.INSTANCE.getNodeId() + ","))) {
                return;
            }
            try {
                startTask(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (event.getStatus().isStopped()) {
            try {
                stopTask(Task.fromConfig(event));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void stopTask(String taskId, String... swimlaneId) {
        if (WORKER_MAP.containsKey(taskId)) {
            TaskWorker worker = WORKER_MAP.get(taskId);
            //停止worker的某个work
            worker.stopJob(swimlaneId);
            //如果worker没有work可做就解雇worker
            stopWorkerWhenNoWork(worker, taskId);
        }
    }

    private void startTask(TaskConfig task) {
        TaskWorker worker = WORKER_MAP.computeIfAbsent(task.getTaskId(), s -> new TaskWorker());
        //尝试通过ClusterProvider的分布式锁功能锁定资源。
        try {
            worker.alloc(task);
            worker.start();
        } catch (Exception e) {
            NodeLog.upload(task.getTaskId(), NodeLog.LogType.TASK_ALARM, e.getMessage());
            e.printStackTrace();
            LOGGER.error("failed to start task:{}", JSONObject.toJSONString(task), e);
        }
        //考虑到任务启动失败造成的worker闲置(内存、线程),检查worker是否有工作，如果空闲，释放worker资源
        stopWorkerWhenNoWork(worker, task.getTaskId());
    }

    private boolean stop() {
        if (stat.compareAndSet(true, false)) {
            LOGGER.info("监工下线.......");
            WORKER_MAP.keySet().stream().collect(Collectors.toList()).forEach(k -> {
                TaskWorker worker = WORKER_MAP.getOrDefault(k, null);
                if (null != worker) worker.stop();
                WORKER_MAP.remove(k);
            });
            return true;
        } else {
            LOGGER.warn("Task controller has stopped already");
            return false;
        }
    }

    private void stopTask(Task task) {
        List<String> swimlaneIdList = task.getConsumers().stream().map(DataConsumer::getSwimlaneId).collect(Collectors.toList());
        stopTask(task.getTaskId(), swimlaneIdList.toArray(new String[0]));
    }

    private void stopWorkerWhenNoWork(TaskWorker worker, String taskId) {
        if (worker.isNoWork()) {
            synchronized (WORKER_MAP) {
                if (worker.isNoWork()) {
                    worker.stop();
                    WORKER_MAP.remove(taskId);
                }
            }
        }
    }



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
}