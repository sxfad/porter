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

import cn.vbill.middleware.porter.common.dic.NodeStatusType;

import java.util.Date;

/**
 * 节点信息表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class Nodes implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

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
    private String machineName;

    /**
     * ip地址.
     */
    private String ipAddress;

    /**
     * 进程号.
     */
    private String pidNumber;

    /**
     * 心跳时间.
     */
    private String heartBeatTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 节点类型.
     */
    private String nodeType;

    /**
     * 节点任务推送状态 .
     */
    private NodeStatusType taskPushState;

    /**
     * 创建人.
     */
    private Long creater;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 是否作废.
     */
    private Integer iscancel;

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
    public String getMachineName() {
        return machineName == null ? null : machineName.trim();
    }

    /**
     * 机器名 set方法.
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName == null ? null : machineName.trim();
    }

    /**
     * ip地址 get方法.
     */
    public String getIpAddress() {
        return ipAddress == null ? null : ipAddress.trim();
    }

    /**
     * ip地址 set方法.
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    /**
     * 进程号 get方法.
     */
    public String getPidNumber() {
        return pidNumber == null ? null : pidNumber.trim();
    }

    /**
     * 进程号 set方法.
     */
    public void setPidNumber(String pidNumber) {
        this.pidNumber = pidNumber == null ? null : pidNumber.trim();
    }

    /**
     * 心跳时间 get方法.
     */
    public String getHeartBeatTime() {
        return heartBeatTime;
    }

    /**
     * 心跳时间 set方法.
     */
    public void setHeartBeatTime(String heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public NodeStatusType getTaskPushState() {
        return taskPushState;
    }

    public void setTaskPushState(NodeStatusType taskPushState) {
        this.taskPushState = taskPushState;
    }

    /**
     * 创建人 get方法.
     */
    public Long getCreater() {
        return creater;
    }

    /**
     * 创建人 set方法.
     */
    public void setCreater(Long creater) {
        this.creater = creater;
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

}
