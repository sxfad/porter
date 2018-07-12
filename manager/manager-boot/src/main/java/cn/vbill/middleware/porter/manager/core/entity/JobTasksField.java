package cn.vbill.middleware.porter.manager.core.entity;

/**
 * 任务数据字段对照关系表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public class JobTasksField implements java.io.Serializable {

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
     * 任务表对照关系id.
     */
    private Long jobTasksTableId;

    /**
     * 来源数据-数据表名-记录全名.
     */
    private String sourceTableName;

    /**
     * 来源数据-数据表字段名称.
     */
    private String sourceTableField;

    /**
     * 目标数据-数据表名-记录全名.
     */
    private String targetTableName;

    /**
     * 目标字段-数据表字段名称.
     */
    private String targetTableField;

    /**
     * 顺序.
     */
    private Long sortOrder;

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
     * 任务表对照关系id get方法.
     */
    public Long getJobTasksTableId() {
        return jobTasksTableId;
    }

    /**
     * 任务表对照关系id set方法.
     */
    public void setJobTasksTableId(Long jobTasksTableId) {
        this.jobTasksTableId = jobTasksTableId;
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
     * 来源数据-数据表字段名称 get方法.
     */
    public String getSourceTableField() {
        return sourceTableField == null ? null : sourceTableField.trim();
    }

    /**
     * 来源数据-数据表字段名称 set方法.
     */
    public void setSourceTableField(String sourceTableField) {
        this.sourceTableField = sourceTableField == null ? null : sourceTableField.trim();
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

    /**
     * 目标字段-数据表字段名称 get方法.
     */
    public String getTargetTableField() {
        return targetTableField == null ? null : targetTableField.trim();
    }

    /**
     * 目标字段-数据表字段名称 set方法.
     */
    public void setTargetTableField(String targetTableField) {
        this.targetTableField = targetTableField == null ? null : targetTableField.trim();
    }

    /**
     * 顺序 get方法.
     */
    public Long getSortOrder() {
        return sortOrder;
    }

    /**
     * 顺序 set方法.
     */
    public void setSortOrder(Long sortOrder) {
        this.sortOrder = sortOrder;
    }
}
