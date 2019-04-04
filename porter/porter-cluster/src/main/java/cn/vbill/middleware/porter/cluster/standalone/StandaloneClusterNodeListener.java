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

import cn.vbill.middleware.porter.cluster.CommonCodeBlock;
import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListenerFilter;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.*;
import cn.vbill.middleware.porter.common.statistics.DNode;
import cn.vbill.middleware.porter.common.cluster.event.ClusterTreeNodeEvent;
import cn.vbill.middleware.porter.common.cluster.event.executor.NodeStopTaskEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.executor.NodeTaskAssignedEventExecutor;
import cn.vbill.middleware.porter.common.cluster.impl.standalone.StandaloneListener;
import cn.vbill.middleware.porter.common.node.dic.NodeHealthLevel;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

/**
 * 节点监听
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月15日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月15日 10:09
 */
public class StandaloneClusterNodeListener extends StandaloneListener {
    private static final String ZK_PATH = BASE_CATALOG + "/node";

    private final ScheduledExecutorService heartbeatWorker =
            Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("node-heartbeat"));

    private static final String STAT_PATH = "/stat";
    private static final String TASK_PATH = "/task/";

    private CommonCodeBlock blockProxy;
    @Override
    public void setClient(ClusterClient client) {
        super.setClient(client);
        blockProxy = new CommonCodeBlock(client);
    }

    @Override
    public String listenPath() {
        return ZK_PATH;
    }

    @Override
    public void onEvent(ClusterTreeNodeEvent event) {
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
                return false;
            }
        };
    }

    @Override
    public List<ClusterListenerEventExecutor> watchedEvents() {
        List<ClusterListenerEventExecutor> executors = new ArrayList<>();
        //任务上传事件
        executors.add(new NodeStopTaskEventExecutor(this.getClass(), NodeContext.INSTANCE.getNodeId(), listenPath()));
        //任务已经被分配
        executors.add(new NodeTaskAssignedEventExecutor(this.getClass(), NodeContext.INSTANCE.getNodeId(), listenPath()));


        //节点停止
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.Shutdown).bind((clusterCommand, client) -> {
            NodeContext.INSTANCE.syncNodeStatus(NodeStatusType.SUSPEND);
            client.delete(listenPath() + "/" + NodeContext.INSTANCE.getNodeId() + "/lock");
            heartbeatWorker.shutdownNow();
        }, client));



        //节点注册
        executors.add(new ClusterListenerEventExecutor(this.getClass(), ClusterListenerEventType.NodeRegister).bind(new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                NodeRegisterCommand nrCommend = (NodeRegisterCommand) clusterCommand;
                NodeContext.INSTANCE.syncUploadStatistic(nrCommend.isUploadStatistic());
                //重置任务状态
                NodeContext.INSTANCE.resetHealthLevel();
                String nodePath = listenPath() + "/" + nrCommend.getId();
                String lockPath = nodePath + "/lock";
                String statPath = nodePath + STAT_PATH;
                client.createRoot(nodePath, false);
                if (!client.isExists(lockPath, false)) {
                    client.create(lockPath, false, new DNode(NodeContext.INSTANCE.getNodeId()).toString());
                    client.create(statPath, new DNode(NodeContext.INSTANCE.getNodeId()).toString(), false, false);

                    /**
                     * 定时一分钟上传一次心跳
                     */
                    heartbeatWorker.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                synchronized (statPath.intern()) {
                                    ClusterClient.TreeNode node = client.isExists(statPath, false) ? client.getData(statPath) : null;
                                    if (null != node && null != node.getVersion()) {
                                        DNode nodeData = DNode.fromString(node.getData(), DNode.class);
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
                                        client.setData(statPath, nodeData.toString(), client.exists(statPath, false));
                                    }
                                }
                            } catch (Exception e) {
                                logger.warn("上传节点心跳出错", e);
                            }
                        }
                    }, 10, 30, TimeUnit.SECONDS);
                } else {
                    if (blockProxy.nodeAssignCheck(lockPath)) {
                        this.accept(clusterCommand, client);
                    } else {
                        String lockPathMsg = lockPath + ",节点已注册";
                        logger.error(lockPathMsg);
                        throw new Exception(lockPathMsg);
                    }
                }
            }
        }, client));
        return executors;
    }
}