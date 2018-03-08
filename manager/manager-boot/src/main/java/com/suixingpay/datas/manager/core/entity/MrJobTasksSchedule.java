package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 任务泳道进度表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class MrJobTasksSchedule implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 任务泳道. */
    private String consumeLane;

    /** 分配节点. */
    private String assignNode;

    /** 数据表. */
    private String dataTable;

    /** 注册时间. */
    private Date registerTime;

    /** 最后心跳时间. */
    private Date heartBeatDate;

    /** 处理进度. */
    private String disposeSchedule;

    /** 告警次数. */
    private String alarmNumber;

    /** 插入次数. */
    private String insertNumber;

    /** 更新次数. */
    private String updateNumber;

    /** 删除次数(成功/失败). */
    private String delNumber;

    /** 创建人. */
    private Long createUserId;

    /** 修改人. */
    private Long updateUserId;

    /** 创建时间. */
    private Date createTime;

    /** 修改时间. */
    private Date updateTime;

    /** 状态. */
    private Integer state;

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

    /** 任务泳道 get方法. */
    public String getConsumeLane() {
        return consumeLane == null ? null : consumeLane.trim();
    }

    /** 任务泳道 set方法. */
    public void setConsumeLane(String consumeLane) {
        this.consumeLane = consumeLane == null ? null : consumeLane.trim();
    }

    /** 分配节点 get方法. */
    public String getAssignNode() {
        return assignNode == null ? null : assignNode.trim();
    }

    /** 分配节点 set方法. */
    public void setAssignNode(String assignNode) {
        this.assignNode = assignNode == null ? null : assignNode.trim();
    }

    /** 数据表 get方法. */
    public String getDataTable() {
        return dataTable == null ? null : dataTable.trim();
    }

    /** 数据表 set方法. */
    public void setDataTable(String dataTable) {
        this.dataTable = dataTable == null ? null : dataTable.trim();
    }

    /** 注册时间 get方法. */
    public Date getRegisterTime() {
        return registerTime;
    }

    /** 注册时间 set方法. */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /** 最后心跳时间 get方法. */
    public Date getHeartBeatDate() {
        return heartBeatDate;
    }

    /** 最后心跳时间 set方法. */
    public void setHeartBeatDate(Date heartBeatDate) {
        this.heartBeatDate = heartBeatDate;
    }

    /** 处理进度 get方法. */
    public String getDisposeSchedule() {
        return disposeSchedule == null ? null : disposeSchedule.trim();
    }

    /** 处理进度 set方法. */
    public void setDisposeSchedule(String disposeSchedule) {
        this.disposeSchedule = disposeSchedule == null ? null : disposeSchedule.trim();
    }

    /** 告警次数 get方法. */
    public String getAlarmNumber() {
        return alarmNumber == null ? null : alarmNumber.trim();
    }

    /** 告警次数 set方法. */
    public void setAlarmNumber(String alarmNumber) {
        this.alarmNumber = alarmNumber == null ? null : alarmNumber.trim();
    }

    /** 插入次数 get方法. */
    public String getInsertNumber() {
        return insertNumber == null ? null : insertNumber.trim();
    }

    /** 插入次数 set方法. */
    public void setInsertNumber(String insertNumber) {
        this.insertNumber = insertNumber == null ? null : insertNumber.trim();
    }

    /** 更新次数 get方法. */
    public String getUpdateNumber() {
        return updateNumber == null ? null : updateNumber.trim();
    }

    /** 更新次数 set方法. */
    public void setUpdateNumber(String updateNumber) {
        this.updateNumber = updateNumber == null ? null : updateNumber.trim();
    }

    /** 删除次数(成功/失败) get方法. */
    public String getDelNumber() {
        return delNumber == null ? null : delNumber.trim();
    }

    /** 删除次数(成功/失败) set方法. */
    public void setDelNumber(String delNumber) {
        this.delNumber = delNumber == null ? null : delNumber.trim();
    }

    /** 创建人 get方法. */
    public Long getCreateUserId() {
        return createUserId;
    }

    /** 创建人 set方法. */
    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    /** 修改人 get方法. */
    public Long getUpdateUserId() {
        return updateUserId;
    }

    /** 修改人 set方法. */
    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    /** 创建时间 get方法. */
    public Date getCreateTime() {
        return createTime;
    }

    /** 创建时间 set方法. */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /** 修改时间 get方法. */
    public Date getUpdateTime() {
        return updateTime;
    }

    /** 修改时间 set方法. */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /** 状态 get方法. */
    public Integer getState() {
        return state;
    }

    /** 状态 set方法. */
    public void setState(Integer state) {
        this.state = state;
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
