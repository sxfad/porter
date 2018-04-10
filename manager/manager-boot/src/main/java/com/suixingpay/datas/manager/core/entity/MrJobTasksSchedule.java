package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.manager.core.util.ResourceUtils;

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

    public MrJobTasksSchedule() {
        
    }

    public MrJobTasksSchedule(DTaskStat stat) {
        //任务id
        this.jobId = stat.getTaskId();
        //任务泳道
        this.swimlaneId = stat.getSwimlaneId();
        //节点id.
        this.nodeId = stat.getNodeId();
        //节点id[ip]
        this.nodeIdIp = ResourceUtils.obtainNodeIp(stat.getNodeId());
        //数据表
        this.schemaTable = stat.getSchema()+stat.getTable();
        //注册时间
        this.registerTime = stat.getRegisteredTime();
        //最后心跳时间
        this.heartBeatDate = stat.getHeartbeatTime();
        //告警次数
        this.alarmNumber = stat.getAlertedTimes().longValue();
        //最近告警检查时间
        this.lastCheckedTime = stat.getLastCheckedTime();
        //插入次数success.
        this.insertSuccess = stat.getInsertRow().longValue();
        //插入次数failure.
        this.insertFailure = stat.getErrorInsertRow().longValue();
        //更新次数success.
        this.updateSuccess = stat.getUpdateRow().longValue();
        //更新次数failure.
        this.updateFailure = stat.getErrorUpdateRow().longValue();
        //删除次数success.
        this.deleteSuccess = stat.getDeleteRow().longValue();
        //删除次数failure.
        this.deleteFailure = stat.getErrorDeleteRow().longValue();
        //处理进度
        this.disposeSchedule = stat.getProgress();
        //最近导入数据时间.
        this.lastLoadedDataTime = stat.getLastLoadedDataTime();
        //最近导入系统时间.
        this.lastLoadedSystemTime = stat.getLastLoadedSystemTime();
        //分区预留字段=注册时间
        this.partitionDay = stat.getRegisteredTime();
        //修改时间
        this.updateTime = new Date();
    }

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private String jobId;

    /**
     * 任务泳道.
     */
    private String swimlaneId;

    /**
     * 节点id.
     */
    private String nodeId;

    /**
     * 节点id[ip].
     */
    private String nodeIdIp;

    /**
     * 数据表.
     */
    private String schemaTable;

    /**
     * 注册时间.
     */
    private Date registerTime;

    /**
     * 最后心跳时间.
     */
    private Date heartBeatDate;

    /**
     * 告警次数.
     */
    private Long alarmNumber;

    /**
     * 最近告警检查时间.
     */
    private Date lastCheckedTime;

    /**
     * 插入次数success.
     */
    private Long insertSuccess;

    /**
     * 插入次数failure.
     */
    private Long insertFailure;

    /**
     * 更新次数success.
     */
    private Long updateSuccess;

    /**
     * 更新次数failure.
     */
    private Long updateFailure;

    /**
     * 删除次数success.
     */
    private Long deleteSuccess;

    /**
     * 删除次数failure.
     */
    private Long deleteFailure;

    /**
     * 处理进度.
     */
    private String disposeSchedule;

    /**
     * 最近导入数据时间.
     */
    private Date lastLoadedDataTime;

    /**
     * 最近导入系统时间.
     */
    private Date lastLoadedSystemTime;

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
     * 任务id get方法.
     */
    public String getJobId() {
        return jobId == null ? null : jobId.trim();
    }

    /**
     * 任务id set方法.
     */
    public void setJobId(String jobId) {
        this.jobId = jobId == null ? null : jobId.trim();
    }

    /**
     * 任务泳道 get方法.
     */
    public String getSwimlaneId() {
        return swimlaneId == null ? null : swimlaneId.trim();
    }

    /**
     * 任务泳道 set方法.
     */
    public void setSwimlaneId(String swimlaneId) {
        this.swimlaneId = swimlaneId == null ? null : swimlaneId.trim();
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
     * 节点id[ip] get方法.
     */
    public String getNodeIdIp() {
        return nodeIdIp == null ? null : nodeIdIp.trim();
    }

    /**
     * 节点id[ip] set方法.
     */
    public void setNodeIdIp(String nodeIdIp) {
        this.nodeIdIp = nodeIdIp == null ? null : nodeIdIp.trim();
    }

    /**
     * 数据表 get方法.
     */
    public String getSchemaTable() {
        return schemaTable == null ? null : schemaTable.trim();
    }

    /**
     * 数据表 set方法.
     */
    public void setSchemaTable(String schemaTable) {
        this.schemaTable = schemaTable == null ? null : schemaTable.trim();
    }

    /**
     * 注册时间 get方法.
     */
    public Date getRegisterTime() {
        return registerTime;
    }

    /**
     * 注册时间 set方法.
     */
    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    /**
     * 最后心跳时间 get方法.
     */
    public Date getHeartBeatDate() {
        return heartBeatDate;
    }

    /**
     * 最后心跳时间 set方法.
     */
    public void setHeartBeatDate(Date heartBeatDate) {
        this.heartBeatDate = heartBeatDate;
    }

    /**
     * 告警次数 get方法.
     */
    public Long getAlarmNumber() {
        return alarmNumber;
    }

    /**
     * 告警次数 set方法.
     */
    public void setAlarmNumber(Long alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

    /**
     * 最近告警检查时间 get方法.
     */
    public Date getLastCheckedTime() {
        return lastCheckedTime;
    }

    /**
     * 最近告警检查时间 set方法.
     */
    public void setLastCheckedTime(Date lastCheckedTime) {
        this.lastCheckedTime = lastCheckedTime;
    }

    /**
     * 插入次数success get方法.
     */
    public Long getInsertSuccess() {
        return insertSuccess;
    }

    /**
     * 插入次数success set方法.
     */
    public void setInsertSuccess(Long insertSuccess) {
        this.insertSuccess = insertSuccess;
    }

    /**
     * 插入次数failure get方法.
     */
    public Long getInsertFailure() {
        return insertFailure;
    }

    /**
     * 插入次数failure set方法.
     */
    public void setInsertFailure(Long insertFailure) {
        this.insertFailure = insertFailure;
    }

    /**
     * 更新次数success get方法.
     */
    public Long getUpdateSuccess() {
        return updateSuccess;
    }

    /**
     * 更新次数success set方法.
     */
    public void setUpdateSuccess(Long updateSuccess) {
        this.updateSuccess = updateSuccess;
    }

    /**
     * 更新次数failure get方法.
     */
    public Long getUpdateFailure() {
        return updateFailure;
    }

    /**
     * 更新次数failure set方法.
     */
    public void setUpdateFailure(Long updateFailure) {
        this.updateFailure = updateFailure;
    }

    /**
     * 删除次数success get方法.
     */
    public Long getDeleteSuccess() {
        return deleteSuccess;
    }

    /**
     * 删除次数success set方法.
     */
    public void setDeleteSuccess(Long deleteSuccess) {
        this.deleteSuccess = deleteSuccess;
    }

    /**
     * 删除次数failure get方法.
     */
    public Long getDeleteFailure() {
        return deleteFailure;
    }

    /**
     * 删除次数failure set方法.
     */
    public void setDeleteFailure(Long deleteFailure) {
        this.deleteFailure = deleteFailure;
    }

    /**
     * 处理进度 get方法.
     */
    public String getDisposeSchedule() {
        return disposeSchedule == null ? null : disposeSchedule.trim();
    }

    /**
     * 处理进度 set方法.
     */
    public void setDisposeSchedule(String disposeSchedule) {
        this.disposeSchedule = disposeSchedule == null ? null : disposeSchedule.trim();
    }

    /**
     * 最近导入数据时间 get方法.
     */
    public Date getLastLoadedDataTime() {
        return lastLoadedDataTime;
    }

    /**
     * 最近导入数据时间 set方法.
     */
    public void setLastLoadedDataTime(Date lastLoadedDataTime) {
        this.lastLoadedDataTime = lastLoadedDataTime;
    }

    /**
     * 最近导入系统时间 get方法.
     */
    public Date getLastLoadedSystemTime() {
        return lastLoadedSystemTime;
    }

    /**
     * 最近导入系统时间 set方法.
     */
    public void setLastLoadedSystemTime(Date lastLoadedSystemTime) {
        this.lastLoadedSystemTime = lastLoadedSystemTime;
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

}
