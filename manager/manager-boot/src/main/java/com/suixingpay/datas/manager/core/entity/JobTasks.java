package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

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

    /** 主键. */
    private Long id;

    /** 任务名称. */
    private String jobName;

    /** 来源数据-消费插件. */
    private Integer sourceConsumeAdt;

    /** 来源数据-消息转换插件. */
    private Integer sourceConvertAdt;

    /** 来源数据-数据源类型. */
    private Integer sourceDataType;

    /** 来源数据-数据源. */
    private Long sourceDataSource;

    /** 目标数据-载入插件. */
    private Integer targetLoadAdt;

    /** 目标数据-载入源类型. */
    private Integer targetDataType;

    /** 目标数据-数据源. */
    private Long targetDataSource;

    /** 创建人. */
    private Long creater;

    /** 创建时间. */
    private Date createTime;

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

    /** 任务名称 get方法. */
    public String getJobName() {
        return jobName == null ? null : jobName.trim();
    }

    /** 任务名称 set方法. */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /** 来源数据-消费插件 get方法. */
    public Integer getSourceConsumeAdt() {
        return sourceConsumeAdt;
    }

    /** 来源数据-消费插件 set方法. */
    public void setSourceConsumeAdt(Integer sourceConsumeAdt) {
        this.sourceConsumeAdt = sourceConsumeAdt;
    }

    /** 来源数据-消息转换插件 get方法. */
    public Integer getSourceConvertAdt() {
        return sourceConvertAdt;
    }

    /** 来源数据-消息转换插件 set方法. */
    public void setSourceConvertAdt(Integer sourceConvertAdt) {
        this.sourceConvertAdt = sourceConvertAdt;
    }

    /** 来源数据-数据源类型 get方法. */
    public Integer getSourceDataType() {
        return sourceDataType;
    }

    /** 来源数据-数据源类型 set方法. */
    public void setSourceDataType(Integer sourceDataType) {
        this.sourceDataType = sourceDataType;
    }

    /** 来源数据-数据源 get方法. */
    public Long getSourceDataSource() {
        return sourceDataSource;
    }

    /** 来源数据-数据源 set方法. */
    public void setSourceDataSource(Long sourceDataSource) {
        this.sourceDataSource = sourceDataSource;
    }

    /** 目标数据-载入插件 get方法. */
    public Integer getTargetLoadAdt() {
        return targetLoadAdt;
    }

    /** 目标数据-载入插件 set方法. */
    public void setTargetLoadAdt(Integer targetLoadAdt) {
        this.targetLoadAdt = targetLoadAdt;
    }

    /** 目标数据-载入源类型 get方法. */
    public Integer getTargetDataType() {
        return targetDataType;
    }

    /** 目标数据-载入源类型 set方法. */
    public void setTargetDataType(Integer targetDataType) {
        this.targetDataType = targetDataType;
    }

    /** 目标数据-数据源 get方法. */
    public Long getTargetDataSource() {
        return targetDataSource;
    }

    /** 目标数据-数据源 set方法. */
    public void setTargetDataSource(Long targetDataSource) {
        this.targetDataSource = targetDataSource;
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
