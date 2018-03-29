package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

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
    private String jobJson;

    /**
     * 创建人.
     */
    private Long createUserId;

    /**
     * 修改人.
     */
    private Long updateUserId;

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
    public String getJobJson() {
        return jobJson == null ? null : jobJson.trim();
    }

    /**
     * 任务json信息 set方法.
     */
    public void setJobJson(String jobJson) {
        this.jobJson = jobJson == null ? null : jobJson.trim();
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
