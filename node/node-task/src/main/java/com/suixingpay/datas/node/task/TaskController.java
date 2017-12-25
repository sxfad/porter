package com.suixingpay.datas.node.task;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 20:53
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.cluster.ClusterProvider;
import com.suixingpay.datas.common.task.Task;
import com.suixingpay.datas.common.task.TaskEvent;
import com.suixingpay.datas.common.task.TaskEventType;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.node.task.worker.TaskWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final Map<String,TaskWorker> WORKER_MAP = new ConcurrentHashMap<>();
    public void start() {
        start(null);
    }

    public void start(List<Task> initTasks) {
        if (stat.compareAndSet(false, true)) {
            if (null != initTasks && !initTasks.isEmpty()) {
                for (Task t : initTasks) {
                    startTask(t);
                }
            }
        } else {
            LOGGER.warn("Task controller has started already");
        }

        //进程退出钩子
        final TaskController controller = this;
        Runtime.getRuntime().addShutdownHook(new Thread("suixingpay-task-shutdownHook"){
            @Override
            public void run() {
                controller.stop();
                //退出群聊需在业务代码执行之后才能执行
                LOGGER.info("退出群聊.......");
                ClusterProvider.unload();
            }
        });

        //监听任务变更事件
        ClusterProvider.addTaskListener(this);
    }

    public void startTask(Task task) {
        //ConcurrentHashMap returns null when value not exists before.
        TaskWorker worker = WORKER_MAP.putIfAbsent(task.getTaskId(),new TaskWorker());
        if (null == worker) worker = WORKER_MAP.get(task.getTaskId());
        //尝试通过ClusterProvider的分布式锁功能锁定资源。
        worker.alloc(task);
        worker.start();
    }

    public void stopTask(String taskId) {
        TaskWorker worker = WORKER_MAP.containsKey(taskId) ? WORKER_MAP.get(taskId) : null;
        stopTask(worker);
    }
    public void stopTask(TaskWorker worker) {
        if (null != worker) {
            worker.stop();
            WORKER_MAP.remove(worker);
        }
    }

    private void stop() {
        if (stat.compareAndSet(true, false)) {
            LOGGER.info("监工下线.......");
            for (TaskWorker worker : WORKER_MAP.values()) {
                stopTask(worker);
            }
        } else {
            LOGGER.warn("Task controller has stoped already");
        }
    }

    @Override
    public void onEvent(TaskEvent event) {
        if (event.getType() == TaskEventType.CREATE) {
            //startTask();
        } else if (event.getType() == TaskEventType.DELETE) {
            //stopTask();
        }
    }
}
