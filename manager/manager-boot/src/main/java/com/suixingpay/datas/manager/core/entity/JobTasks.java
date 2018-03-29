package com.suixingpay.datas.manager.core.entity;

import com.suixingpay.datas.common.dic.TaskStatusType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 同步任务表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class JobTasks implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务名称.
     */
    private String jobName;

    /**
     * 任务状态.
     */
    private TaskStatusType jobState;

    /**
     * 来源数据-消费插件.
     */
    private String sourceConsumeAdt;

    /**
     * 来源数据-消费转换插件.
     */
    private String sourceConvertAdt;

    /**
     * 来源数据-元数据表分组id.
     */
    private Long sourceDataTablesId;

    /**
     * 来源数据-元数据表分组名称.
     */
    private String sourceDataTablesName;

    /**
     * 来源数据-同步数据源id.(kafka\cancl)
     */
    private Long sourceDataId;

    /**
     * 来源数据-同步数据源名称.
     */
    private String sourceDataName;

    /**
     * 目标数据-载入插件.
     */
    private String targetLoadAdt;

    /**
     * 目标数据-载入源id.
     */
    private Long targetDataTablesId;

    /**
     * 目标数据-载入源名称.
     */
    private String targetDataTablesName;

    /**
     * 创建人.
     */
    private Long creater;

    /**
     * 创建时间.
     */
    private Date createTime;

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
     * 告警人id列表.
     */
    private List<Long> userIds = new ArrayList<>();

    /**
     * 告警人信息列表
     */
    private List<CUser> users = new ArrayList<>();

    /**
     * 表对照关系.
     */
    private List<JobTasksTable> tables = new ArrayList<>();

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
     * 任务名称 get方法.
     */
    public String getJobName() {
        return jobName == null ? null : jobName.trim();
    }

    /**
     * 任务名称 set方法.
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 任务状态 get方法.
     */
    public TaskStatusType getJobState() {
        return jobState;
    }

    /**
     * 任务状态 set方法.
     */
    public void setJobState(TaskStatusType jobState) {
        this.jobState = jobState;
    }

    /**
     * 来源数据-消费插件 get方法.
     */
    public String getSourceConsumeAdt() {
        return sourceConsumeAdt == null ? null : sourceConsumeAdt.trim();
    }

    /**
     * 来源数据-消费插件 set方法.
     */
    public void setSourceConsumeAdt(String sourceConsumeAdt) {
        this.sourceConsumeAdt = sourceConsumeAdt == null ? null : sourceConsumeAdt.trim();
    }

    /**
     * 来源数据-消费转换插件 get方法.
     */
    public String getSourceConvertAdt() {
        return sourceConvertAdt == null ? null : sourceConvertAdt.trim();
    }

    /**
     * 来源数据-消费转换插件 set方法.
     */
    public void setSourceConvertAdt(String sourceConvertAdt) {
        this.sourceConvertAdt = sourceConvertAdt == null ? null : sourceConvertAdt.trim();
    }

    /**
     * 来源数据-元数据表分组id get方法.
     */
    public Long getSourceDataTablesId() {
        return sourceDataTablesId;
    }

    /**
     * 来源数据-元数据表分组id set方法.
     */
    public void setSourceDataTablesId(Long sourceDataTablesId) {
        this.sourceDataTablesId = sourceDataTablesId;
    }

    /**
     * 来源数据-元数据表分组名称 get方法.
     */
    public String getSourceDataTablesName() {
        return sourceDataTablesName == null ? null : sourceDataTablesName.trim();
    }

    /**
     * 来源数据-元数据表分组名称 set方法.
     */
    public void setSourceDataTablesName(String sourceDataTablesName) {
        this.sourceDataTablesName = sourceDataTablesName == null ? null : sourceDataTablesName.trim();
    }

    /**
     * 来源数据-同步数据源id get方法.
     */
    public Long getSourceDataId() {
        return sourceDataId;
    }

    /**
     * 来源数据-同步数据源id set方法.
     */
    public void setSourceDataId(Long sourceDataId) {
        this.sourceDataId = sourceDataId;
    }

    /**
     * 来源数据-同步数据源名称 get方法.
     */
    public String getSourceDataName() {
        return sourceDataName == null ? null : sourceDataName.trim();
    }

    /**
     * 来源数据-同步数据源名称 set方法.
     */
    public void setSourceDataName(String sourceDataName) {
        this.sourceDataName = sourceDataName == null ? null : sourceDataName.trim();
    }

    /**
     * 目标数据-载入插件 get方法.
     */
    public String getTargetLoadAdt() {
        return targetLoadAdt == null ? null : targetLoadAdt.trim();
    }

    /**
     * 目标数据-载入插件 set方法.
     */
    public void setTargetLoadAdt(String targetLoadAdt) {
        this.targetLoadAdt = targetLoadAdt == null ? null : targetLoadAdt.trim();
    }

    /**
     * 目标数据-载入数据组id get方法.
     */
    public Long getTargetDataTablesId() {
        return targetDataTablesId;
    }

    /**
     * 目标数据-载入数据组id set方法.
     */
    public void setTargetDataTablesId(Long targetDataTablesId) {
        this.targetDataTablesId = targetDataTablesId;
    }

    /**
     * 目标数据-载入数据组名称 get方法.
     */
    public String getTargetDataTablesName() {
        return targetDataTablesName == null ? null : targetDataTablesName.trim();
    }

    /**
     * 目标数据-载入数据组名称 set方法.
     */
    public void setTargetDataTablesName(String targetDataTablesName) {
        this.targetDataTablesName = targetDataTablesName == null ? null : targetDataTablesName.trim();
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

    /** 告警人id列表 get方法. */
    public List<Long> getUserIds() {
        return userIds;
    }

    /** 告警人id列表 set方法. */
    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds;
    }

    /**
     * 告警人信息列表 get方法.
     */
    public List<CUser> getUsers() {
        return users;
    }

    /**
     * 告警人信息列表 set方法.
     */
    public void setUsers(List<CUser> users) {
        this.users = users;
    }

    public List<JobTasksTable> getTables() {
        return tables;
    }

    public void setTables(List<JobTasksTable> tables) {
        this.tables = tables;
    }
}
