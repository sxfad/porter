/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.cluster.zookeeper;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.cluster.ClusterListenerFilter;
import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;
import com.suixingpay.datas.common.cluster.command.ShutdownCommand;
import com.suixingpay.datas.common.cluster.command.TaskAssignedCommand;
import com.suixingpay.datas.common.cluster.command.TaskStopCommand;
import com.suixingpay.datas.common.cluster.command.broadcast.NodeRegister;
import com.suixingpay.datas.common.cluster.command.broadcast.Shutdown;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskAssigned;
import com.suixingpay.datas.common.cluster.command.broadcast.TaskStop;
import com.suixingpay.datas.common.cluster.event.ClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListener;
import com.suixingpay.datas.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import com.suixingpay.datas.common.cluster.data.DNode;
import com.suixingpay.datas.common.config.NodeCommandConfig;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.dic.NodeHealthLevel;
import com.suixingpay.datas.common.node.NodeCommandType;
import com.suixingpay.datas.common.dic.NodeStatusType;
import com.suixingpay.datas.common.task.TaskEventListener;
import com.suixingpay.datas.common.task.TaskEventProvider;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.common.util.MachineUtils;
import com.suixingpay.datas.node.core.NodeContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.data.Stat;

import java.util.*;
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
                                //通知数据到zookeeper
                                client.setData(statPath, nodeData.toString(), dataPair.getRight().getVersion());
                            }
                        }
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
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
            return false;
        }
    }

    private boolean isTaskStoppedByError(String taskId, String swimlaneId) {
        String lockPath = BASE_CATALOG + "/task/" + taskId + "/error/" + swimlaneId;
        try {
            Stat lockStat = client.exists(lockPath, false);
            return null != lockStat;
        } catch (Exception e) {
            return false;
        }
    }

    private DNode getDNode(String nodePath) {
        Pair<String, Stat> dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getLeft(), DNode.class);
    }
}