package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

import com.suixingpay.datas.common.statistics.NodeLog;

/**
 * 日志监控信息表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class MrLogMonitor implements java.io.Serializable {

    public MrLogMonitor() {
        
    }

    public MrLogMonitor(NodeLog log) {
        this.nodeId = log.getNodeId();
        this.jobId = log.getTaskId();
        this.jobName = log.getTaskId()+"name";
    }

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
     * 任务id.
     */
    private String jobId;

    /**
     * 任务标题.
     */
    private String jobName;

    /**
     * IP地址.
     */
    private String ipAdress;

    /**
     * 日志时间.
     */
    private Date logDate;

    /**
     * 日志标题.
     */
    private String logTitle;

    /**
     * 日志内容.
     */
    private String logContent;

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
     * 预留分区字段.
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
     * 任务id get方法.
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * 任务id set方法.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    /**
     * 任务标题 get方法.
     */
    public String getJobName() {
        return jobName == null ? null : jobName.trim();
    }

    /**
     * 任务标题 set方法.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * IP地址 get方法.
     */
    public String getIpAdress() {
        return ipAdress == null ? null : ipAdress.trim();
    }

    /**
     * IP地址 set方法.
     */
    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress == null ? null : ipAdress.trim();
    }

    /**
     * 日志时间 get方法.
     */
    public Date getLogDate() {
        return logDate;
    }

    /**
     * 日志时间 set方法.
     */
    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    /**
     * 日志标题 get方法.
     */
    public String getLogTitle() {
        return logTitle == null ? null : logTitle.trim();
    }

    /**
     * 日志标题 set方法.
     */
    public void setLogTitle(String logTitle) {
        this.logTitle = logTitle == null ? null : logTitle.trim();
    }

    /**
     * 日志内容 get方法.
     */
    public String getLogContent() {
        return logContent == null ? null : logContent.trim();
    }

    /**
     * 日志内容 set方法.
     */
    public void setLogContent(String logContent) {
        this.logContent = logContent == null ? null : logContent.trim();
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
     * 预留分区字段 get方法.
     */
    public Date getPartitionDay() {
        return partitionDay;
    }

    /**
     * 预留分区字段 set方法.
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

}
