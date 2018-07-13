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

package cn.vbill.middleware.porter.manager.core.entity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import cn.vbill.middleware.porter.manager.core.init.ResourceUtils;
import com.alibaba.fastjson.JSON;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.dic.NodeHealthLevel;

/**
 * 节点任务监控表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class MrNodesSchedule implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public MrNodesSchedule() {

    }

    public MrNodesSchedule(DNode node) {
        // 节点id
        this.nodeId = node.getNodeId();
        // 机器名.
        this.computerName = node.getHostName();
        // ipAddress
        this.ipAddress = node.getAddress();
        // 心跳时间.
        this.heartBeatDate = node.getHeartbeat();
        // 进程号.
        this.processNumber = node.getProcessId();
        // 任务json信息.
        this.jobIdJson = JSON.toJSONString(node.getTasks());
        // 任务json-name信息.
        this.jobNameJson = jobNameJson(node.getTasks());
        // 预留时间分区字段
        // this.partitionDay = null;
        // 健康等级
        this.healthLevel = node.getHealthLevel();
        // 健康等级描述
        this.healthLevelDesc = node.getHealthLevelDesc();
        // 修改时间
        this.updateTime = new Date();
    }

    private String jobNameJson(Map<String, TreeSet<String>> tasks) {
        Map<String, TreeSet<String>> taskmap = new HashMap<String, TreeSet<String>>();
        for (Map.Entry<String, TreeSet<String>> entry : tasks.entrySet()) {
            String jobName = ResourceUtils.obtainJobName(entry.getKey());
            taskmap.put(jobName != null ? jobName : "(空-id(" + entry.getKey() + "))", entry.getValue());
        }
        return JSON.toJSONString(taskmap);
    }

    /**
     * 主键.
     */
    private Long id;

    /**
     * 节点id.
     */
    private String nodeId;

    /**
     * 机器名.
     */
    private String computerName;

    /**
     * ip_address.
     */
    private String ipAddress;

    /**
     * 心跳时间.
     */
    private Date heartBeatDate;

    /**
     * 进程号.
     */
    private String processNumber;

    /**
     * 任务json信息.
     */
    private String jobIdJson;

    /**
     * 任务json-name信息.
     */
    private String jobNameJson;

    /**
     * 创建人.
     */
    private Long createUserId;

    /**
     * 修改人.
     */
    private Long updateUserId;

    /**
     * 健康等级
     */
    private NodeHealthLevel healthLevel;

    /**
     * 健康描述
     */
    private String healthLevelDesc;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 预留时间分区字段.
     */
    private Date partitionDay;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 节点id get方法.
     */
    public String getNodeId() {
        return nodeId == null ? null : nodeId.trim();
    }

    /**
     * 节点id set方法.
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    /**
     * 机器名 get方法.
     */
    public String getComputerName() {
        return computerName == null ? null : computerName.trim();
    }

    /**
     * 机器名 set方法.
     */
    public void setComputerName(String computerName) {
        this.computerName = computerName == null ? null : computerName.trim();
    }

    /**
     * ip_address get方法.
     */
    public String getIpAddress() {
        return ipAddress == null ? null : ipAddress.trim();
    }

    /**
     * ip_address set方法.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    /**
     * 心跳时间 get方法.
     */
    public Date getHeartBeatDate() {
        return heartBeatDate;
    }

    /**
     * 心跳时间 set方法.
     */
    public void setHeartBeatDate(Date heartBeatDate) {
        this.heartBeatDate = heartBeatDate;
    }

    /**
     * 进程号 get方法.
     */
    public String getProcessNumber() {
        return processNumber == null ? null : processNumber.trim();
    }

    /**
     * 进程号 set方法.
     */
    public void setProcessNumber(String processNumber) {
        this.processNumber = processNumber == null ? null : processNumber.trim();
    }

    /**
     * 任务json信息 get方法.
     */
    public String getJobIdJson() {
        return jobIdJson == null ? null : jobIdJson.trim();
    }

    /**
     * 任务json信息 set方法.
     */
    public void setJobIdJson(String jobIdJson) {
        this.jobIdJson = jobIdJson == null ? null : jobIdJson.trim();
    }

    /**
     * 任务json-name信息 get方法.
     */
    public String getJobNameJson() {
        return jobNameJson == null ? null : jobNameJson.trim();
    }

    /**
     * 任务json-name信息 set方法.
     */
    public void setJobNameJson(String jobNameJson) {
        this.jobNameJson = jobNameJson == null ? null : jobNameJson.trim();
    }

    /**
     * 创建人 get方法.
     */
    public Long getCreateUserId() {
        return createUserId;
    }

    /**
     * 创建人 set方法.
     */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /**
     * 修改人 get方法.
     */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /**
     * 修改人 set方法.
     */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /**
     * 创建时间 get方法.
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间 set方法.
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间 get方法.
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间 set方法.
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 状态 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 是否作废 get方法.
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废 set方法.
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /**
     * 预留时间分区字段 get方法.
     */
    public Date getPartitionDay() {
        return partitionDay;
    }

    /**
     * 预留时间分区字段 set方法.
     */
    public void setPartitionDay(Date partitionDay) {
        this.partitionDay = partitionDay;
    }

    /**
     * 备注 get方法.
     */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /**
     * 备注 set方法.
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
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
}
