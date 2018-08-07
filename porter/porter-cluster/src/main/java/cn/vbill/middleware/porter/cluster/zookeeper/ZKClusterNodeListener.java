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

package cn.vbill.middleware.porter.cluster.zookeeper;

import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.command.NodeRegisterCommand;
import cn.vbill.middleware.porter.common.cluster.command.ShutdownCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskAssignedCommand;
import cn.vbill.middleware.porter.common.cluster.command.TaskStopCommand;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.NodeRegister;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.Shutdown;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskAssigned;
import cn.vbill.middleware.porter.common.cluster.command.broadcast.TaskStop;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.cluster.event.ClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.config.NodeCommandConfig;
import cn.vbill.middleware.porter.common.config.TaskConfig;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.node.NodeCommandType;
import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.task.TaskEventProvider;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * 节点监听
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class ZKClusterNodeListener extends ZookeeperClusterListener  implements TaskEventProvider, NodeRegister,
        Shutdown, TaskAssigned, TaskStop {
    private static final String ZK_PATH = BASE_CATALOG + "/node";
    private static final Pattern NODE_ORDER_PATTERN = Pattern.compile(ZK_PATH + "/.*/order/.*");
    private static final Pattern NODE_STAT_PATTERN = Pattern.compile(ZK_PATH + "/.*/stat");
    private static final Pattern NODE_LOCK_PATTERN = Pattern.compile(ZK_PATH + "/.*/lock");

    private final List<TaskEventListener> TASK_LISTENER = new ArrayList<>();
    private final ScheduledExecutorService heartbeatWorker =
            Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("node-heartbeat"));

    private static final Logger LOGGER = LoggerFactory.getLogger(ZKClusterNodeListener.class);

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
        ZookeeperClusterEvent zkEvent = (ZookeeperClusterEvent) event;
        LOGGER.debug("集群节点监听->{},{},{}", zkEvent.getPath(), zkEvent.getData(), zkEvent.getEventType());
        //下发命令
        if (NODE_ORDER_PATTERN.matcher(zkEvent.getPath()).matches()) {
            NodeCommandConfig commandConfig = JSONObject.parseObject(zkEvent.getData(), NodeCommandConfig.class);
            //释放当前节点正在执行的任务
            if (commandConfig.getCommand() == NodeCommandType.RELEASE_WORK) {
                //查询出来当前节点正在执行的任务
                DNode nodeData = getDNode(listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + "/stat");
                if (null != nodeData) {
                    //遍历节点任务
                    nodeData.getTasks().forEach((taskId, swimlaneId) -> {
                        swimlaneId.forEach(s -> {
                            //获取任务配置
                            TaskConfig config = getTaskConfig(taskId, s);
                            if (null != config) {
                                //设置为停止任务配置文件
                                config.setStatus(TaskStatusType.STOPPED);
                                //停止任务
                                triggerTaskEvent(config);
                            }
                        });
                    });
                }
            }

            //节点状态变更
            if (commandConfig.getCommand() == NodeCommandType.CHANGE_STATUS) {
                NodeStatusType oldStat = NodeContext.INSTANCE.getNodeStatus();
                NodeContext.INSTANCE.syncNodeStatus(commandConfig.getStatus());
                //节点开始接收新任务
                if (commandConfig.getStatus().isWorking() && !oldStat.isWorking()) {
                    String taskNode = BASE_CATALOG + "/task";
                    List<String> taskPathList = client.getChildren(taskNode);
                    taskPathList.forEach(id -> {
                        List<String> distPathList = client.getChildren(taskNode + "/" + id + "/dist");
                        distPathList.forEach(swimlaneId -> {
                            TaskConfig taskConfig = getTaskConfig(id, swimlaneId);
                            if (!isTaskStoppedByError(id, swimlaneId) && !isTaskLocked(id, swimlaneId)
                                    && null != taskConfig && taskConfig.getStatus().isWorking()) {
                                //分配任务
                                triggerTaskEvent(taskConfig);
                            }
                        });
                    });
                }
            }

            //调整节点可执行工作数量
            if (commandConfig.getCommand() == NodeCommandType.WORK_LIMIT
                    && null != commandConfig.getWorkLimit() && commandConfig.getWorkLimit() > -1) {
                NodeContext.INSTANCE.updateWorkLimit(commandConfig.getWorkLimit());
            }
            //删除指令
            client.delete(zkEvent.getPath());
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
                boolean access = false;
                //仅仅监控应用自身
                //event.getEventType() == EventType.ONLINE &&
                if (event.getPath().contains(getPath() + "/" + NodeContext.INSTANCE.getNodeId())) {
                    access = true;
                }
                return access;
            }
        };
    }

    @Override
    public void nodeRegister(NodeRegisterCommand nrCommend) throws Exception {
        NodeContext.INSTANCE.syncNodeId(nrCommend.getId());
        NodeContext.INSTANCE.syncUploadStatistic(nrCommend.isUploadStatistic());
        //重置任务状态
        NodeContext.INSTANCE.resetHealthLevel();
        String nodePath = listenPath() + "/" + nrCommend.getId();
        String lockPath = nodePath + "/lock";
        String statPath = nodePath + "/stat";

        client.createWhenNotExists(nodePath, false, false, null);

        Stat stat = client.exists(lockPath, false);
        if (null == stat) {
            client.create(lockPath, false, new DNode(NodeContext.INSTANCE.getNodeId()).toString());
            client.createWhenNotExists(statPath, false, false, new DNode(NodeContext.INSTANCE.getNodeId()).toString());

            /**
             * 定时一分钟上传一次心跳
             */
            heartbeatWorker.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        synchronized (statPath.intern()) {
                            Pair<String, Stat> dataPair = client.getData(statPath);
                            if (null != dataPair && null != dataPair.getRight()) {
                                DNode nodeData = DNode.fromString(dataPair.getLeft(), DNode.class);
                                //设置心跳时间
                                nodeData.setHeartbeat(new Date());
                                //设置节点工作状态
                                nodeData.setStatus(NodeContext.INSTANCE.getNodeStatus());
                                //设置节点健康状态
                                Pair<NodeHealthLevel, String> level = NodeContext.INSTANCE.getHealthLevel();
                                nodeData.setHealthLevel(level.getLeft());
                                nodeData.setHealthLevelDesc(level.getRight());
                                //设置机器信息
                                nodeData.setAddress(MachineUtils.IP_ADDRESS);
                                nodeData.setProcessId(MachineUtils.CURRENT_JVM_PID + "");
                                nodeData.setHostName(MachineUtils.HOST_NAME);
                                NodeContext.INSTANCE.flushClusterNode(nodeData);
                                //通知数据到zookeeper
                                client.setData(statPath, nodeData.toString(), dataPair.getRight().getVersion());
                            }
                        }
                    } catch (KeeperException e) {
                        e.printStackTrace();
                        LOGGER.error("%s", e);
                    } catch (InterruptedException e) {
                        Thread.interrupted();
                        e.printStackTrace();
                    }
                }
            }, 10, 30, TimeUnit.SECONDS);
        } else {
            throw  new Exception(lockPath + ",节点已注册");
        }
    }

    @Override
    public void shutdown(ShutdownCommand command) {
        NodeContext.INSTANCE.syncNodeStatus(NodeStatusType.SUSPEND);
        client.delete(listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + "/lock");
        heartbeatWorker.shutdownNow();
    }

    @Override
    public void taskAssigned(TaskAssignedCommand command) throws Exception {
        String path = listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + "/stat";
        synchronized (path.intern()) {
            DNode nodeData = getDNode(path);
            TreeSet<String> resources = nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>());
            resources.add(command.getSwimlaneId());
            nodeData.getTasks().put(command.getTaskId(), resources);
            Stat nowStat = client.exists(path, true);
            client.setData(path, nodeData.toString(), nowStat.getVersion());
        }
    }

    @Override
    public void stopTask(TaskStopCommand command) throws Exception {
        String path = listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + "/stat";
        synchronized (path.intern()) {
            DNode nodeData = getDNode(path);
            if (null != nodeData.getTasks() && !nodeData.getTasks().isEmpty()) {
                TreeSet<String> swimlaneIdList = nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>());
                if (swimlaneIdList.contains(command.getSwimlaneId())) swimlaneIdList.remove(command.getSwimlaneId());
                if (swimlaneIdList.isEmpty()) nodeData.getTasks().remove(command.getTaskId());
            }
            Stat nowStat = client.exists(path, true);
            client.setData(path, nodeData.toString(), nowStat.getVersion());
        }
    }

    private void triggerTaskEvent(TaskConfig event) {
        TASK_LISTENER.forEach(l -> l.onEvent(event));
    }

    @Override
    public void addTaskEventListener(TaskEventListener listener) {
        TASK_LISTENER.add(listener);
    }

    @Override
    public void removeTaskEventListener(TaskEventListener listener) {
        TASK_LISTENER.remove(listener);
    }

    private TaskConfig getTaskConfig(String taskId, String swimlaneId) {
        String swimPath = BASE_CATALOG + "/task/" + taskId + "/dist/" + swimlaneId;
        TaskConfig config = null;
        Pair<String, Stat> taskConfigPair = client.getData(swimPath);
        if (null != taskConfigPair && !StringUtils.isBlank(taskConfigPair.getLeft())) {
            //将json转换为java对象
            config = JSONObject.parseObject(taskConfigPair.getLeft(), TaskConfig.class);
        }
        return config;
    }

    private boolean isTaskLocked(String taskId, String swimlaneId) {
        String lockPath = BASE_CATALOG + "/task/" + taskId + "/lock/" + swimlaneId;
        try {
            Stat lockStat = client.exists(lockPath, true);
            return null != lockStat;
        } catch (Exception e) {
            LOGGER.error("%s", e);
            return false;
        }
    }

    private boolean isTaskStoppedByError(String taskId, String swimlaneId) {
        String lockPath = BASE_CATALOG + "/task/" + taskId + "/error/" + swimlaneId;
        try {
            Stat lockStat = client.exists(lockPath, false);
            return null != lockStat;
        } catch (Exception e) {
            LOGGER.error("%s", e);
            return false;
        }
    }

    private DNode getDNode(String nodePath) {
        Pair<String, Stat> dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getLeft(), DNode.class);
    }
}