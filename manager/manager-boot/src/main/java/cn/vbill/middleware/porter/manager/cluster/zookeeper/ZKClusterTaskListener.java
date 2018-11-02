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
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.statistics.NodeLog;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.client.impl.ZookeeperClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.broadcast.ZKTaskPush;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.dic.TaskStatusType;
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
public class ZKClusterTaskListener extends ZookeeperClusterListener implements ZKTaskPush {

    private static final String ZK_PATH = BASE_CATALOG + "/task";
    private static final Pattern TASK_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat/.*");
    private static final Pattern TASK_ERROR_PATTERN = Pattern.compile(ZK_PATH + "/.*/error/.*");
    private static final Pattern TASK_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock/.*");
    private static final Pattern TASK_DIST_PATTERN = Pattern.compile(ZK_PATH + "/.*/dist/.*");

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterTaskListener.class);
    // 未分配任务定时检查
    private final ScheduledExecutorService taskUnsignedListener = Executors.newSingleThreadScheduledExecutor();

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        String zkPath = zkEvent.getPath();
        LOGGER.debug("TaskListener:{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        try {
            // 任务进度更新
            if (TASK_STAT_PATTERN.matcher(zkPath).matches() && zkEvent.isDataChanged()) {
                DTaskStat stat = DTaskStat.fromString(zkEvent.getData(), DTaskStat.class);
                LOGGER.info("4-DTaskStat.... " + JSON.toJSON(stat));
                // do something
                try {
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.dealDTaskStat(stat);
                } catch (Exception e) {
                    LOGGER.error("4-DTaskStat-Error....出错,请追寻...", e);
                }
            }
            // 任务错误
            if (TASK_ERROR_PATTERN.matcher(zkEvent.getPath()).matches()) {
                String[] taskAndSwimlane = null;
                try {
                    taskAndSwimlane = zkPath.replace(listenPath(), "").substring(1).split("/error/");
                } catch (Throwable e) {
                    LOGGER.error("zk任务错误消息解析失败！", e);
                }
                if (null == taskAndSwimlane || taskAndSwimlane.length != 2) {
                    LOGGER.error("zk任务错误消息未解析出合规的内容 [{}]", JSON.toJSONString(taskAndSwimlane));
                    return;
                }
                if (zkEvent.isDataChanged() || zkEvent.isOnline()) {
                    ManagerContext.INSTANCE.newStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    LOGGER.info("zk任务错误消息DataChanged or Online,内容:[{}]", JSON.toJSONString(taskAndSwimlane));
                    return;
                }
                if (zkEvent.isOffline()) {
                    LOGGER.info("zk任务错误消息Offline,内容:[{}]", JSON.toJSONString(taskAndSwimlane));
                    ManagerContext.INSTANCE.removeStoppedTask(taskAndSwimlane[0], taskAndSwimlane[1]);
                    return;
                }
            }
            // 监控任务上线
            if (TASK_LOCK_PATTERN.matcher(zkEvent.getPath()).matches() && event.isOnline()) {
                try {
                    String taskId = DealStrCutUtils.getSubUtilSimple(zkPath, "task/(.*?)/lock");
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.updateState(taskId == null ? 0 : Long.valueOf(taskId),
                            TaskStatusType.WORKING);
                    LOGGER.info("4-DTaskStat-[{}]任务启动.", taskId);
                } catch (Exception e) {
                    LOGGER.error("4-DTaskStat-Error-lock....任务启动出错,请追寻...", e);
                }
            }
            // 监控任务下线
            if (TASK_LOCK_PATTERN.matcher(zkEvent.getPath()).matches() && event.isOffline()) {
                try {
                    String taskId = DealStrCutUtils.getSubUtilSimple(zkPath, "task/(.*?)/lock");
                    MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                            .getBean(MrJobTasksScheduleServiceImpl.class);
                    mrJobTasksScheduleService.updateState(taskId == null ? 0 : Long.valueOf(taskId),
                            TaskStatusType.STOPPED);
                    LOGGER.info("4-DTaskStat-[{}]任务停止.", taskId);
                } catch (Exception e) {
                    LOGGER.error("4-DTaskStat-Error-lock....任务停止出错,请追寻...", e);
                }
            }
            // 抓取任务配置信息
            if (TASK_DIST_PATTERN.matcher(zkEvent.getPath()).matches() && event.isOnline()) {
                try {
                    TaskConfig task = JSONObject.parseObject(zkEvent.getData(), TaskConfig.class);
                    if (task.isLocalTask()) {
                        MrJobTasksScheduleService mrJobTasksScheduleService = ApplicationContextUtil
                                .getBean(MrJobTasksScheduleServiceImpl.class);
                        mrJobTasksScheduleService.dealJobJsonText(task, zkEvent.getData());
                        LOGGER.info("4-DTaskStat-dist-本地任务配置抓取-[{}]", zkEvent.getData());
                    }
                } catch (Exception e) {
                    LOGGER.error("4-DTaskStat-Error-dist....本地任务配置抓取出错,请追寻...", e);
                }
            }
        } catch (Throwable e) {
            LOGGER.error("4-DTaskStat-Throwable....出错,请追寻...", e);
        }
    }

    @Override
    public ClusterListenerFilter filter() {
        return new ZookeeperClusterListenerFilter() {

            @Override
            protected String getPath() {
                return listenPath();
            }

            @Override
            protected boolean doFilter(ZookeeperClusterEvent event) {
                return true;
            }
        };
    }

    @Override
    public void push(TaskPushCommand command) throws Exception {
        push(command, true, true);
    }

    @Override
    public ZookeeperClient getZKClient() {
        return client;
    }

    @Override
    public String zkTaskPath() {
        return ZK_PATH;
    }

    @Override
    public void start() {
        // 在cluster模块初始化30分钟后每10分钟检查一次未被节点消费的任务
        taskUnsignedListener.scheduleAtFixedRate(() -> {
            String msg = unsignedTaskMsg();
            if (null != msg && !msg.trim().isEmpty()) {
                NodeLog log = new NodeLog();
                log.setError(msg);
                log.setTitle("【管理员告警】已发布节点未分配任务列表");
                log.setType(NodeLog.LogType.TASK_WARNING);
                NodeLog.upload(log, null);
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
                Stat stat = client.exists(dist, true);
                if (null != stat) {
                    List<String> canals = client.getChildren(dist);
                    for (String canal : canals) {
                        Pair<String, Stat> taskContent = client.getData(dist + "/" + canal);
                        if (null != taskContent.getLeft() && !taskContent.getLeft().isEmpty())
                            taskConfigs.add(JSONObject.parseObject(taskContent.getLeft(), TaskConfig.class));
                    }
                }
            } catch (Throwable e) {
                LOGGER.warn("list deployed task error", e);
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
            try {
                List<SourceConfig> sourceConfigs = SourceConfig.getConfig(config.getConsumer().getSource()).swamlanes();
                for (SourceConfig source : sourceConfigs) {
                    String swimlaneLock = ZK_PATH + "/" + config.getTaskId() + "/lock/" + source.getSwimlaneId();
                    if (config.getStatus() == TaskStatusType.WORKING && null == client.exists(swimlaneLock, true)) {
                        messages.append("预分配节点:").append(config.getNodeId()).append(",任务Id:").append(config.getTaskId())
                                .append(",泳道Id:").append(source.getSwimlaneId()).append(System.lineSeparator());
                    }
                }
            } catch (Throwable e) {
                LOGGER.warn("list unsigned swimlane error", e);
            }
        }
        return messages.toString();
    }

}