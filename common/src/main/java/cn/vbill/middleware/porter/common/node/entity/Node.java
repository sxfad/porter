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

package cn.vbill.middleware.porter.common.node.entity;

import cn.vbill.middleware.porter.common.statistics.DNode;
import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.node.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.node.dic.NodeHealthLevel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 17:29
 */
public class Node {
    //节点ID
    private String nodeId;
    //节点状态
    private NodeStatusType status = NodeStatusType.SUSPEND;
    //是否上传统计信息
    private boolean uploadStatistic = true;
    //节点健康级别
    private NodeHealthLevel healthLevel = NodeHealthLevel.GREEN;
    //节点健康级别描述
    private String healthLevelDesc;
    //工作总阀值
    private int workLimit = 5;
    //工作指标用量
    private AtomicInteger workUsed = new AtomicInteger(0);
    private volatile DNode dnodeSnapshot = new DNode();

    //工作模式
    private ClusterPlugin workMode = ClusterPlugin.STANDALONE;


    //强制任务分配
    public static String FORCE_ASSIGN_SIGN = "--force";
    private boolean forceAssign = false;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public NodeStatusType getStatus() {
        return status;
    }

    public void setStatus(NodeStatusType status) {
        this.status = status;
    }

    public boolean isUploadStatistic() {
        return uploadStatistic;
    }

    public void setUploadStatistic(boolean uploadStatistic) {
        this.uploadStatistic = uploadStatistic;
    }

    public NodeHealthLevel getHealthLevel() {
        return healthLevel;
    }

    public void setHealthLevel(NodeHealthLevel healthLevel) {
        this.healthLevel = healthLevel;
    }

    public String getHealthLevelDesc() {
        return healthLevelDesc;
    }

    public void setHealthLevelDesc(String healthLevelDesc) {
        this.healthLevelDesc = healthLevelDesc;
    }

    public int getWorkLimit() {
        return workLimit;
    }

    public void setWorkLimit(int workLimit) {
        this.workLimit = workLimit;
    }

    public AtomicInteger getWorkUsed() {
        return workUsed;
    }

    public void setWorkUsed(AtomicInteger workUsed) {
        this.workUsed = workUsed;
    }

    public DNode getDnodeSnapshot() {
        return dnodeSnapshot;
    }

    public void setDnodeSnapshot(DNode dnodeSnapshot) {
        this.dnodeSnapshot = dnodeSnapshot;
    }

    public ClusterPlugin getWorkMode() {
        return workMode;
    }

    public void setWorkMode(ClusterPlugin workMode) {
        this.workMode = workMode;
    }

    public boolean isForceAssign() {
        return forceAssign;
    }

    public void setForceAssign(boolean forceAssign) {
        this.forceAssign = forceAssign;
    }
}
