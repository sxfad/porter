package com.suixingpay.datas.manager.core.entity;

import com.suixingpay.datas.common.dic.NodeStatusType;

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

    /** 主键. */
    private Long id;

    /** 节点id. */
    private String nodeId;

    /** 机器名. */
    private String machineName;

    /** ip地址. */
    private String ipAddress;

    /** 进程号. */
    private String pidNumber;

    /** 心跳时间. */
    private Date heartBeatTime;

    /** 状态. */
    private Integer state;

    /** 节点类型. */
    private NodeStatusType type;

    /** 创建人. */
    private Long creater;

    /** 创建时间. */
    private Date createTime;

    /** 是否作废. */
    private Integer iscancel;

    /** 备注. */
    private String remark;

    /** 主键 get方法. */
    public Long getId() {
        return id;
    }

    /** 主键 set方法. */
    public void setId(Long id) {
        this.id = id;
    }

    /** 节点id get方法. */
    public String getNodeId() {
        return nodeId == null ? null : nodeId.trim();
    }

    /** 节点id set方法. */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

    /** 机器名 get方法. */
    public String getMachineName() {
        return machineName == null ? null : machineName.trim();
    }

    /** 机器名 set方法. */
    public void setMachineName(String machineName) {
        this.machineName = machineName == null ? null : machineName.trim();
    }

    /** ip地址 get方法. */
    public String getIpAddress() {
        return ipAddress == null ? null : ipAddress.trim();
    }

    /** ip地址 set方法. */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress == null ? null : ipAddress.trim();
    }

    /** 进程号 get方法. */
    public String getPidNumber() {
        return pidNumber == null ? null : pidNumber.trim();
    }

    /** 进程号 set方法. */
    public void setPidNumber(String pidNumber) {
        this.pidNumber = pidNumber == null ? null : pidNumber.trim();
    }

    /** 心跳时间 get方法. */
    public Date getHeartBeatTime() {
        return heartBeatTime;
    }

    /** 心跳时间 set方法. */
    public void setHeartBeatTime(Date heartBeatTime) {
        this.heartBeatTime = heartBeatTime;
    }

    /** 状态 get方法. */
    public Integer getState() {
        return state;
    }

    /** 状态 set方法. */
    public void setState(Integer state) {
        this.state = state;
    }

    /** 节点类型 get方法. */
    public NodeStatusType getType() {
        return type;
    }

    /** 节点类型 set方法. */
    public void setType(NodeStatusType type) {
        this.type = type;
    }

    /** 创建人 get方法. */
    public Long getCreater() {
        return creater;
    }

    /** 创建人 set方法. */
    public void setCreater(Long creater) {
        this.creater = creater;
    }

    /** 创建时间 get方法. */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 set方法. */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 是否作废 get方法. */
    public Integer getIscancel() {
        return iscancel;
    }

    /** 是否作废 set方法. */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /** 备注 get方法. */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /** 备注 set方法. */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

}
