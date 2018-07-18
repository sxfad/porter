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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;
import cn.vbill.middleware.porter.common.node.Node;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
public enum  NodeContext {
    INSTANCE();
    private final ReadWriteLock nodeLock = new ReentrantReadWriteLock();
    private final Node node = new Node();

    private final Map<String, String> taskErrorMarked = new ConcurrentHashMap<>();
    private final Map<String, Object> consumeProcess = new ConcurrentHashMap<>();
    private final Map<String, Object> consumerIdle = new ConcurrentHashMap<>();

    private ApplicationContext context;
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    public void syncNodeId(String nodeId) {
        try {
            nodeLock.writeLock().lock();
            node.setNodeId(nodeId);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public void syncNodeStatus(NodeStatusType status) {
        try {
            nodeLock.writeLock().lock();
            node.setStatus(status);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public String getNodeId() {
        try {
            nodeLock.readLock().lock();
            return node.getNodeId();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public NodeStatusType getNodeStatus() {
        try {
            nodeLock.readLock().lock();
            return node.getStatus();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public void syncUploadStatistic(boolean uploadStatistic) {
        try {
            nodeLock.writeLock().lock();
            node.setUploadStatistic(uploadStatistic);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public boolean isUploadStatistic() {
        try {
            nodeLock.readLock().lock();
            return node.isUploadStatistic();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    private void syncHealthLevel(NodeHealthLevel level, String desc) {
        try {
            nodeLock.writeLock().lock();
            node.setHealthLevel(level);
            node.setHealthLevelDesc(desc);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public Pair<NodeHealthLevel, String> getHealthLevel() {
        try {
            nodeLock.readLock().lock();
            return new ImmutablePair<>(node.getHealthLevel(), node.getHealthLevelDesc());
        } finally {
            nodeLock.readLock().unlock();
        }
    }

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
                percent = new Double(nowCounter) / new Double(node.getWorkLimit());
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

    public void removeTaskError(String taskId) {
        taskErrorMarked.remove(taskId);
        if (taskErrorMarked.isEmpty()) {
            syncHealthLevel(NodeHealthLevel.GREEN, "");
        }
    }

    public void markTaskError(String taskId, String e) {
        taskErrorMarked.put(taskId, taskId);
        syncHealthLevel(NodeHealthLevel.RED, e);
    }

    public void flushClusterNode(DNode dnode) {
        node.setDnodeSnapshot(dnode);
    }

    public void flushConsumeProcess(String key, String position) {
        consumeProcess.put(key, position);
    }

    public void flushConsumerIdle(String taskId, String swimlaneId, long secondsTime) {
        if (secondsTime > 0) {
            consumerIdle.put(taskId + "_" + swimlaneId, secondsTime + "s");
        } else {
            consumerIdle.remove(taskId + "_" + swimlaneId);
        }
    }

    public Map<String, String> getTaskErrorMarked() {
        return Collections.unmodifiableMap(taskErrorMarked);
    }

    public String dumpNode() {
        JSONObject object = (JSONObject) JSON.toJSON(node);
        object.put("consumeProcess", Collections.unmodifiableMap(consumeProcess));
        object.put("consumerIdle", Collections.unmodifiableMap(consumerIdle));
        return object.toJSONString();
    }
    public String[] getEnvironment() {
        return context.getEnvironment().getActiveProfiles();
    }
}
