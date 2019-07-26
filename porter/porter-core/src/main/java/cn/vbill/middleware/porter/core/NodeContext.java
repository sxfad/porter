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

package cn.vbill.middleware.porter.core;

import cn.vbill.middleware.porter.common.statistics.DNode;
import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.node.dic.NodeHealthLevel;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.node.entity.Node;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
public enum NodeContext {

    /**
     * INSTANCE
     */
    INSTANCE();
    private final ReadWriteLock nodeLock = new ReentrantReadWriteLock();
    private final Node node = new Node();

    private final Map<List<String>, WarningMessage> taskErrorMarked = new ConcurrentHashMap<>();
    private final Map<String, Object> consumeProcess = new ConcurrentHashMap<>();
    private final Map<String, Object> consumerIdle = new ConcurrentHashMap<>();
    private final List<String> startupArgs = new ArrayList<>();

    private volatile ApplicationContext context;

    private volatile boolean force;
    private volatile List<WarningReceiver> receivers = new ArrayList<>();

    /**
     * 获取Bean
     *
     * @date 2018/8/8 下午5:22
     * @param: [clazz]
     * @return: T
     */
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    /**
     * syncNodeId
     *
     * @date 2018/8/8 下午5:22
     * @param: [nodeId]
     * @return: void
     */
    public void syncNodeId(String nodeId) {
        try {
            nodeLock.writeLock().lock();
            node.setNodeId(nodeId);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    /**
     * NodeStatus
     *
     * @date 2018/8/8 下午5:22
     * @param: [status]
     * @return: void
     */
    public void syncNodeStatus(NodeStatusType status) {
        try {
            nodeLock.writeLock().lock();
            node.setStatus(status);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    /**
     * 获取NodeId
     *
     * @date 2018/8/8 下午5:36
     * @param: []
     * @return: java.lang.String
     */
    public String getNodeId() {
        try {
            nodeLock.readLock().lock();
            return node.getNodeId();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    /**
     * 获取NodeStatus
     *
     * @date 2018/8/8 下午5:36
     * @param: []
     * @return: cn.vbill.middleware.porter.common.dic.NodeStatusType
     */
    public NodeStatusType getNodeStatus() {
        try {
            nodeLock.readLock().lock();
            return node.getStatus();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    /**
     * syncUploadStatistic
     *
     * @date 2018/8/8 下午5:36
     * @param: [uploadStatistic]
     * @return: void
     */
    public void syncUploadStatistic(boolean uploadStatistic) {
        try {
            nodeLock.writeLock().lock();
            node.setUploadStatistic(uploadStatistic);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    /**
     * 是否UploadStatistic
     *
     * @date 2018/8/8 下午5:37
     * @param: []
     * @return: boolean
     */
    public boolean isUploadStatistic() {
        try {
            nodeLock.readLock().lock();
            return node.isUploadStatistic();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    /**
     * syncHealthLevel
     *
     * @date 2018/8/8 下午5:37
     * @param: [level, desc]
     * @return: void
     */
    private void syncHealthLevel(NodeHealthLevel level, String desc) {
        try {
            nodeLock.writeLock().lock();
            node.setHealthLevel(level);
            node.setHealthLevelDesc(desc);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    /**
     * 获取HealthLevel
     *
     * @date 2018/8/8 下午5:37
     * @param: []
     * @return: org.apache.commons.lang3.tuple.Pair<cn.vbill.middleware.porter.common.dic.NodeHealthLevel,java.lang.String>
     */
    public Pair<NodeHealthLevel, String> getHealthLevel() {
        try {
            nodeLock.readLock().lock();
            return new ImmutablePair<>(node.getHealthLevel(), node.getHealthLevelDesc());
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    /**
     * updateWorkLimit
     *
     * @date 2018/8/8 下午5:38
     * @param: [limit]
     * @return: void
     */
    public void updateWorkLimit(Integer limit) {
        try {
            nodeLock.writeLock().lock();
            node.setWorkLimit(limit);
        } finally {
            nodeLock.writeLock().unlock();
        }

        //刷新健康级别
        tagHealthLevelWhenWorkChange();
    }

    /**
     * 粗粒度的node lock加上细粒度的workUsed乐观锁
     * 如果申请work失败，则丧失执行任务的机会
     * 最坏结果是所有运行节点都没有资源用于执行该任务
     *
     * @return
     */
    public boolean acquireWork() {
        boolean acquire = false;
        try {
            nodeLock.writeLock().lock();
            int oldCounter = node.getWorkUsed().get();
            int newCounter = oldCounter + 1;
            acquire = newCounter <= node.getWorkLimit() && node.getWorkUsed().compareAndSet(oldCounter, newCounter);
        } finally {
            nodeLock.writeLock().unlock();
        }
        //只有在成功获取资源后才会刷新
        if (acquire) {
            tagHealthLevelWhenWorkChange();
        }
        return acquire;
    }

    /**
     * 释放work资源
     *
     * @return
     */
    public void releaseWork() {
        try {
            nodeLock.writeLock().lock();
            int released = node.getWorkUsed().decrementAndGet();
            if (released < 0) {
                node.getWorkUsed().compareAndSet(released, 0);
            }
        } finally {
            nodeLock.writeLock().unlock();
        }
        //刷新健康级别
        tagHealthLevelWhenWorkChange();
    }

    /**
     * resetHealthLevel
     *
     * @date 2018/8/8 下午5:39
     * @param: []
     * @return: void
     */
    public void resetHealthLevel() {
        syncHealthLevel(NodeHealthLevel.GREEN, "");
    }


    /**
     * 更新节点健康级别
     */
    private void tagHealthLevelWhenWorkChange() {
        //如果不存在任务运行异常
        if (taskErrorMarked.isEmpty()) {
            double percent = 0;
            try {
                nodeLock.readLock().lock();
                int nowCounter = node.getWorkUsed().get();
                percent = Double.valueOf(nowCounter) / Double.valueOf(node.getWorkLimit());
            } finally {
                nodeLock.readLock().unlock();
            }
            //黄色告警
            if (percent < 0.7) {
                syncHealthLevel(NodeHealthLevel.GREEN, "");
            }
            //黄色告警
            if (percent >= 0.7 && percent < 0.9) {
                syncHealthLevel(NodeHealthLevel.YELLOW, "节点工作资源超70%");
            }
            //红色警报
            if (percent >= 0.9) {
                syncHealthLevel(NodeHealthLevel.RED, "节点工作资源已饱和");
            }
        }
    }

    /**
     * removeTaskError
     *
     * @date 2018/8/8 下午5:39
     * @param: [taskId]
     * @return: void
     */
    public void removeTaskError(List<String> key) {
        taskErrorMarked.remove(key);
        if (taskErrorMarked.isEmpty()) {
            syncHealthLevel(NodeHealthLevel.GREEN, "");
        }
    }

    public void removeTaskError(String taskId) {
        taskErrorMarked.keySet().stream().filter(key -> key.get(0).equals(taskId)).forEach(key -> taskErrorMarked.remove(key));
        if (taskErrorMarked.isEmpty()) {
            syncHealthLevel(NodeHealthLevel.GREEN, "");
        }
    }

    /**
     * markTaskError
     *
     * @date 2018/8/8 下午5:39
     * @param: [taskId, e]
     * @return: void
     */
    public void markTaskError(List<String> key, WarningMessage e) {
        taskErrorMarked.put(key, e);
        syncHealthLevel(NodeHealthLevel.RED, e.getErrorCode().name());
    }

    /**
     * flushClusterNode
     *
     * @date 2018/8/8 下午5:40
     * @param: [dnode]
     * @return: void
     */
    public void flushClusterNode(DNode dnode) {
        node.setDnodeSnapshot(dnode);
    }

    /**
     * flushConsumeProcess
     *
     * @date 2018/8/8 下午5:40
     * @param: [key, position]
     * @return: void
     */
    public void flushConsumeProcess(String taskId, String swimlaneId, String position) {
        consumeProcess.put(taskId + "_" + swimlaneId, position);
    }

    public void clearConsumeProcess(String taskId, String swimlaneId) {
        consumeProcess.remove(taskId + "_" + swimlaneId);
    }

    /**
     * flushConsumerIdle
     *
     * @date 2018/8/8 下午5:40
     * @param: [taskId, swimlaneId, secondsTime]
     * @return: void
     */
    public void flushConsumerIdle(String taskId, String swimlaneId, long secondsTime) {
        if (secondsTime > 0) {
            consumerIdle.put(taskId + "_" + swimlaneId, secondsTime + "s");
        } else {
            consumerIdle.remove(taskId + "_" + swimlaneId);
        }
    }

    public List<String> getTaskErrorMarked() {
        List<String> warning = new ArrayList<>();
        taskErrorMarked.forEach((k, v) -> warning.add(v.getTitle()));
        return warning;
    }

    /**
     * dumpNode
     *
     * @date 2018/8/8 下午5:41
     * @param: []
     * @return: java.lang.String
     */
    public String dumpNode() {
        JSONObject object = (JSONObject) JSON.toJSON(node);
        object.put("consumeProcess", Collections.unmodifiableMap(consumeProcess));
        object.put("consumerIdle", Collections.unmodifiableMap(consumerIdle));
        return object.toJSONString();
    }

    public String[] getEnvironment() {
        return context.getEnvironment().getActiveProfiles();
    }

    public void workMode(ClusterPlugin mode) {
        try {
            nodeLock.writeLock().lock();
            node.setWorkMode(mode);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public ClusterPlugin getWorkMode() {
        try {
            nodeLock.readLock().lock();
            return node.getWorkMode();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public void startupArgs(String[] args) {
        try {
            nodeLock.writeLock().lock();
            startupArgs.addAll(Arrays.asList(args));
            node.setForceAssign(startupArgs.stream().filter(e -> e.equals(Node.FORCE_ASSIGN_SIGN)).count() > 0);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public boolean forceAssign() {
        try {
            nodeLock.readLock().lock();
            return node.isForceAssign();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public String getAddress() {
        try {
            nodeLock.readLock().lock();
            return node.getDnodeSnapshot().getAddress();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public synchronized void addWarningReceivers(WarningReceiver[] newReceivers) {
        receivers.clear();
        receivers.addAll(Arrays.asList(newReceivers));
    }

    public final List<WarningReceiver> getReceivers() {
        return receivers;
    }
}
