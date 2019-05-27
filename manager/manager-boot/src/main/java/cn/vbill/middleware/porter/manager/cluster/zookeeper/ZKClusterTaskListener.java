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

package cn.vbill.middleware.porter.manager.cluster.zookeeper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.event.executor.TaskPushEventExecutor;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.task.statistics.DTaskError;
import cn.vbill.middleware.porter.common.warning.WarningProviderFactory;
import cn.vbill.middleware.porter.common.warning.entity.WarningErrorCode;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.manager.ManagerContext;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.core.util.DealStrCutUtils;
import cn.vbill.middleware.porter.manager.service.MrJobTasksScheduleService;
import cn.vbill.middleware.porter.manager.service.impl.MrJobTasksScheduleServiceImpl;

/**
 * 任务信息监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterTaskListener extends ZookeeperClusterListener {

    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_ERROR_PATTERN = Pattern.compile(ZK_PATH + "/.*/error/.*");
    private static final Pattern TASK_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock/.*");
    private static final Pattern TASK_DIST_PATTERN = Pattern.compile(ZK_PATH + "/.*/dist/.*");

    // 未分配任务定时检查
    private final ScheduledExecutorService taskUnsignedListener = Executors
            .newSingleThreadScheduledExecutor(new ThreadFactory() {
                private final AtomicInteger seq = new AtomicInteger();
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(false);
                    t.setName("UnsignedTask-Listener-" + seq.incrementAndGet());
                    return t;
                }
            });

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent zkEvent) {
        String zkPath = zkEvent.getId();
        logger.debug("TaskListener:{},{},{}", zkPath, zkEvent.getData(), zkEvent.getEventType());
        try {
            // 任务错误
            if (TASK_ERROR_PATTERN.matcher(zkPath).matches()) {
                DTaskError error;
                try {
                    error = DTaskError.fromString(zkEvent.getData(), DTaskError.class);
                } catch (Throwable e) {
                    String[] taskAndSwimlane = zkPath.replace(listenPath(), "").substring(1).split("/error/");
                    error = new DTaskError(taskAndSwimlane[0], taskAndSwimlane[1], zkEvent.getData());
                }

                if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
                    ManagerContext.INSTANCE.newStoppedTask(Arrays.asList(error.getTaskId(), error.getSwimlaneId()),
                            zkEvent.getData());
                    logger.info("zk任务错误消息DataChanged or Online,内容:[{}]", error.getMessage());
                    return;
                }
                if (zkEvent.isOffline()) {
                    logger.info("zk任务错误消息Offline,内容:[{}]", error.getMessage());
                    ManagerContext.INSTANCE.removeStoppedTask(Arrays.asList(error.getTaskId(), error.getSwimlaneId()));
                    return;
                }
            }
            // 监控任务上线
            if (TASK_LOCK_PATTERN.matcher(zkPath).matches() && zkEvent.isOnline()) {
                try {
                    String taskId = DealStrCutUtils.getSubUtilSimple(zkPath, "task/(.*?)/lock");
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.updateState(taskId == null ? 0 : Long.valueOf(taskId),
                            TaskStatusType.WORKING);
                    logger.info("4-DTaskStat-[{}]任务启动.", taskId);
                } catch (Exception e) {
                    logger.error("4-DTaskStat-Error-lock....任务启动出错,请追寻...", e);
                }
            }
            // 监控任务下线
            if (TASK_LOCK_PATTERN.matcher(zkPath).matches() && zkEvent.isOffline()) {
                try {
                    String taskId = DealStrCutUtils.getSubUtilSimple(zkPath, "task/(.*?)/lock");
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.updateState(taskId == null ? 0 : Long.valueOf(taskId),
                            TaskStatusType.STOPPED);
                    logger.info("4-DTaskStat-[{}]任务停止.", taskId);
                } catch (Exception e) {
                    logger.error("4-DTaskStat-Error-lock....任务停止出错,请追寻...", e);
                }
            }
            // 抓取任务配置信息
            if (TASK_DIST_PATTERN.matcher(zkPath).matches() && zkEvent.isOnline()) {
                try {
                    TaskConfig task = JSONObject.parseObject(zkEvent.getData(), TaskConfig.class);
                    if (task.isLocalTask()) {
                        MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                                .getBean(MrJobTasksScheduleServiceImpl.class);
                        mrJobTasksScheduleService.dealJobJsonText(task, zkEvent.getData());
                        logger.info("4-DTaskStat-dist-本地任务配置抓取-[{}]", zkEvent.getData());
                    }
                } catch (Exception e) {
                    logger.error("4-DTaskStat-Error-dist....本地任务配置抓取出错,请追寻...", e);
                }
            }
        } catch (Throwable e) {
            logger.error("4-DTaskStat-Throwable....出错,请追寻...", e);
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ClusterListenerFilter() {
            @Override
            public String getPath() {
                return listenPath();
            }

            @Override
            public boolean doFilter(ClusterTreeNodeEvent event) {
                return true;
            }
        };
    }

    @Override
    public void start() {
        // 在cluster模块初始化30分钟后每10分钟检查一次未被节点消费的任务
        taskUnsignedListener.scheduleAtFixedRate(() -> {
            String msg = unsignedTaskMsg();
            if (null != msg && !msg.trim().isEmpty()) {
                NodeLog log = new NodeLog(NodeLog.LogType.WARNING, msg).upload();
                try {
                    WarningProviderFactory.INSTANCE.notice(new WarningMessage("【管理员告警】运行状态异常任务列表", log.getError(),
                            WarningErrorCode.match(log.getError()))
                                    .bindReceivers(ManagerContext.INSTANCE.getReceivers()));
                } catch (InterruptedException e) {
                }
            }
        }, 30, 10, TimeUnit.MINUTES);
    }

    /**
     * 查询所有发布的任务
     * 
     * @return
     */
    private List<TaskConfig> deployedTasks() {
        List<TaskConfig> taskConfigs = new ArrayList<>();
        List<String> taskIds = client.getChildren(ZK_PATH);
        for (String id : taskIds) {
            String dist = ZK_PATH + "/" + id + "/dist";
            try {
                ClusterClient.LockVersion stat = client.exists(dist, true);
                if (null != stat) {
                    List<String> canals = client.getChildren(dist);
                    for (String canal : canals) {
                        ClusterClient.TreeNode taskContent = client.getData(dist + "/" + canal);
                        if (null != taskContent.getData() && !taskContent.getData().isEmpty())
                            taskConfigs.add(JSONObject.parseObject(taskContent.getData(), TaskConfig.class));
                    }
                }
            } catch (Throwable e) {
                logger.warn("list deployed task error", e);
            }
        }
        return taskConfigs;
    }

    /**
     * 查询所有已发布但没有被分配的任务
     * 
     * @return
     */
    private String unsignedTaskMsg() {
        StringBuffer messages = new StringBuffer();
        List<TaskConfig> taskConfigs = deployedTasks();
        for (TaskConfig config : taskConfigs) {
            List<SourceConfig> sourceConfigs = null;
            try {
                sourceConfigs = SourceConfig.getConfig(config.getConsumer().getSource()).swamlanes();
            } catch (ConfigParseException e) {
                NodeLog.upload(NodeLog.LogType.WARNING, config.getTaskId(), "", "运行状态检测异常-" + e.getMessage());
                e.printStackTrace();
                continue;
            }
            try {
                for (SourceConfig source : sourceConfigs) {
                    String swimlaneLock = ZK_PATH + "/" + config.getTaskId() + "/lock/" + source.getSwimlaneId();
                    if (config.getStatus() == TaskStatusType.WORKING && !client.isExists(swimlaneLock, true)) {
                        messages.append(System.lineSeparator()).append("预分配节点:").append(config.getNodeId())
                                .append(",任务Id:").append(config.getTaskId()).append(",泳道:")
                                .append(source.getSwimlaneId()).append(System.lineSeparator());
                    }
                }
            } catch (Exception e) {
                logger.warn("list unsigned swimlane error", e);
            }
        }
        return messages.toString();
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();
        // 任务上传事件
        executors.add(new TaskPushEventExecutor(this.getClass(), true, true, listenPath()));
        return executors;
    }

}