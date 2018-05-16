package com.suixingpay.datas.manager.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务数据表对照关系表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public class JobTasksTable implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private Long jobTaskId;

    /**
     * 来源数据-数据表名-记录全名.
     */
    private String sourceTableName;

    /**
     * 目标数据-数据表名-记录全名.
     */
    private String targetTableName;

    /**
     * 忽略目标端大小写
     */
    private Boolean ignoreTargetCase = false;

    /**
     * 强制目标端字段和源端字段一致(针对目标表来说)
     */
    private Boolean forceMatched = true;

    /**
     * 直接映射表，不进行表字段映射配置 默认false
     */
    private Boolean directMapTable = false;

    /**
     * 表字段对应关系
     */
    private List<JobTasksField> fields = new ArrayList<>();

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
    public Long getJobTaskId() {
        return jobTaskId;
    }

    /**
     * 任务id set方法.
     */
    public void setJobTaskId(Long jobTaskId) {
        this.jobTaskId = jobTaskId;
    }

    /**
     * 来源数据-数据表名-记录全名 get方法.
     */
    public String getSourceTableName() {
        return sourceTableName == null ? null : sourceTableName.trim();
    }

    /**
     * 来源数据-数据表名-记录全名 set方法.
     */
    public void setSourceTableName(String sourceTableName) {
        this.sourceTableName = sourceTableName == null ? null : sourceTableName.trim();
    }

    /**
     * 目标数据-数据表名-记录全名 get方法.
     */
    public String getTargetTableName() {
        return targetTableName == null ? null : targetTableName.trim();
    }

    /**
     * 目标数据-数据表名-记录全名 set方法.
     */
    public void setTargetTableName(String targetTableName) {
        this.targetTableName = targetTableName == null ? null : targetTableName.trim();
    }

    public List<JobTasksField> getFields() {
        return fields;
    }

    public void setFields(List<JobTasksField> fields) {
        this.fields = fields;
    }

    public Boolean isIgnoreTargetCase() {
        return ignoreTargetCase;
    }

    public void setIgnoreTargetCase(Boolean ignoreTargetCase) {
        this.ignoreTargetCase = ignoreTargetCase;
    }

    public Boolean isForceMatched() {
        return forceMatched;
    }

    public void setForceMatched(Boolean forceMatched) {
        this.forceMatched = forceMatched;
    }

    public Boolean isDirectMapTable() {
        return directMapTable;
    }

    public void setDirectMapTable(Boolean directMapTable) {
        this.directMapTable = directMapTable;
    }

}
