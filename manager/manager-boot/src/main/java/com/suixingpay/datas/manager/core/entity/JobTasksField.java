package com.suixingpay.datas.manager.core.entity;

/**
 * 任务数据字段对照关系表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class JobTasksField implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 任务id. */
    private Long jobTaskId;

    /** 任务对照表id. */
    private Long jobTableId;

    /** 来源tableid. */
    private Long sourceTableId;

    /** 目标tableid. */
    private Long targetTableId;

    /** 来源字段. */
    private String sourceField;

    /** 目标字段. */
    private String targetField;

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

    /** 任务对照表id get方法. */
    public Long getJobTableId() {
        return jobTableId;
    }

    /** 任务对照表id set方法. */
    public void setJobTableId(Long jobTableId) {
        this.jobTableId = jobTableId;
    }

    /** 来源tableid get方法. */
    public Long getSourceTableId() {
        return sourceTableId;
    }

    /** 来源tableid set方法. */
    public void setSourceTableId(Long sourceTableId) {
        this.sourceTableId = sourceTableId;
    }

    /** 目标tableid get方法. */
    public Long getTargetTableId() {
        return targetTableId;
    }

    /** 目标tableid set方法. */
    public void setTargetTableId(Long targetTableId) {
        this.targetTableId = targetTableId;
    }

    /** 来源字段 get方法. */
    public String getSourceField() {
        return sourceField == null ? null : sourceField.trim();
    }

    /** 来源字段 set方法. */
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField == null ? null : sourceField.trim();
    }

    /** 目标字段 get方法. */
    public String getTargetField() {
        return targetField == null ? null : targetField.trim();
    }

    /** 目标字段 set方法. */
    public void setTargetField(String targetField) {
        this.targetField = targetField == null ? null : targetField.trim();
    }

}
