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

package cn.vbill.middleware.porter.cluster.standalone;

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
import cn.vbill.middleware.porter.common.cluster.impl.standalone.StandaloneListener;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterEvent;
import cn.vbill.middleware.porter.common.cluster.impl.zookeeper.ZookeeperClusterListenerFilter;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 节点监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class StandaloneClusterNodeListener extends StandaloneListener implements NodeRegister,
        Shutdown, TaskAssigned, TaskStop {
    private static final String ZK_PATH = BASE_CATALOG + "/node";

    private final ScheduledExecutorService heartbeatWorker =
            Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("node-heartbeat"));

    private static final Logger LOGGER = LoggerFactory.getLogger(StandaloneClusterNodeListener.class);
    private static final String STAT_PATH = "/stat";
    private static final String TASK_PATH = "/task/";

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterEvent event) {
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
                return false;
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
        String statPath = nodePath + STAT_PATH;
        client.createDir(nodePath);
        if (!client.exists(lockPath, false)) {
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
                            Pair<String, Boolean> dataPair = client.getData(statPath);
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
                                client.setData(statPath, nodeData.toString(), -1);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.warn("上传节点心跳出错", e);
                    }
                }
            }, 10, 30, TimeUnit.SECONDS);
        } else {
            if (nodeAssignCheck(lockPath)) {
                nodeRegister(nrCommend);
            } else {
                String lockPathMsg = lockPath + ",节点已注册";
                LOGGER.error(lockPathMsg);
                throw new Exception(lockPathMsg);
            }
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
        String path = listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + STAT_PATH;
        synchronized (path.intern()) {
            DNode nodeData = getDNode(path);
            TreeSet<String> resources = nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>());
            resources.add(command.getSwimlaneId());
            nodeData.getTasks().put(command.getTaskId(), resources);
            client.setData(path, nodeData.toString(), -1);
        }
    }

    @Override
    public void stopTask(TaskStopCommand command) throws Exception {
        String path = listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + STAT_PATH;
        synchronized (path.intern()) {
            DNode nodeData = getDNode(path);
            if (null != nodeData.getTasks() && !nodeData.getTasks().isEmpty()) {
                TreeSet<String> swimlaneIdList = nodeData.getTasks().getOrDefault(command.getTaskId(), new TreeSet<>());
                if (swimlaneIdList.contains(command.getSwimlaneId())) {
                    swimlaneIdList.remove(command.getSwimlaneId());
                }
                if (swimlaneIdList.isEmpty()) {
                    nodeData.getTasks().remove(command.getTaskId());
                }
            }
            client.setData(path, nodeData.toString(), -1);
        }
    }
    /**
     * 获取Node
     *
     * @date 2018/8/8 下午4:22
     * @param: [nodePath]
     * @return: cn.vbill.middleware.porter.common.cluster.data.DNode
     */
    private DNode getDNode(String nodePath) {
        Pair<String, Boolean> dataPair = client.getData(nodePath);
        return DNode.fromString(dataPair.getLeft(), DNode.class);
    }

    private boolean nodeAssignCheck(String path) {
        try {
            if (!NodeContext.INSTANCE.forceAssign()) return false;
            Pair<String, Boolean> lockPair = client.getData(path);
            if (null != lockPair && StringUtils.isNotBlank(lockPair.getLeft())) {
                DNode nodeDetail = JSONObject.parseObject(lockPair.getLeft(), DNode.class);
                if (nodeDetail.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) //节点Id相符
                        && nodeDetail.getAddress().equals(NodeContext.INSTANCE.getAddress())) { //IP地址相符
                    client.delete(path);
                    return true;
                }
            }
        } catch (Exception e) {
            LOGGER.warn("尝试删除节点占用");
        }
        return false;
    }
}