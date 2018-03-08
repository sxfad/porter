package com.suixingpay.datas.manager.core.entity;

/**
 * 任务数据表对照关系表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class JobTasksTable implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 任务id. */
    private Long jobTaskId;

    /** 来源数据-数据表. */
    private Long sourceTableId;

    /** 来源数据-数据表名称. */
    private String sourceTableName;

    /** 目标数据-数据表. */
    private Long targetTableId;

    /** 目标数据-数据表名称. */
    private String targetTableName;

    /** 主键 get方法. */
    public Long getId() {
        return id;
    }

    /** 主键 set方法. */
    public void setId(Long id) {
        this.id = id;
    }

    /** 任务id get方法. */
    public Long getJobTaskId() {
        return jobTaskId;
    }

    /** 任务id set方法. */
    public void setJobTaskId(Long jobTaskId) {
        this.jobTaskId = jobTaskId;
    }

    /** 来源数据-数据表 get方法. */
    public Long getSourceTableId() {
        return sourceTableId;
    }

    /** 来源数据-数据表 set方法. */
    public void setSourceTableId(Long sourceTableId) {
        this.sourceTableId = sourceTableId;
    }

    /** 来源数据-数据表名称 get方法. */
    public String getSourceTableName() {
        return sourceTableName == null ? null : sourceTableName.trim();
    }

    /** 来源数据-数据表名称 set方法. */
    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName == null ? null : sourceTableName.trim();
    }

    /** 目标数据-数据表 get方法. */
    public Long getTargetTableId() {
        return targetTableId;
    }

    /** 目标数据-数据表 set方法. */
    public void setTargetTableId(Long targetTableId) {
        this.targetTableId = targetTableId;
    }

    /** 目标数据-数据表名称 get方法. */
    public String getTargetTableName() {
        return targetTableName == null ? null : targetTableName.trim();
    }

    /** 目标数据-数据表名称 set方法. */
    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName == null ? null : targetTableName.trim();
    }

}
