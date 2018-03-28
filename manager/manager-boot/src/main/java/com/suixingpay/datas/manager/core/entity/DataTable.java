package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 数据表信息表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class DataTable implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 数据源id.
     */
    private Long sourceId;

    /**
     * 数据源类型(继承).
     */
    private String dataType;

    /**
     * 结构名、库名、前缀名.
     */
    private String bankName;

    /**
     * 表名.
     */
    private String tableName;

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
     * 数据源名称.
     */
    private String sourceName;

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
     * 数据源id get方法.
     */
    public Long getSourceId() {
        return sourceId;
    }

    /**
     * 数据源id set方法.
     */
    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * 数据源类型(继承) get方法.
     */
    public String getDataType() {
        return dataType == null ? null : dataType.trim();
    }

    /**
     * 数据源类型(继承) set方法.
     */
    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    /**
     * 结构名、库名、前缀名 get方法.
     */
    public String getBankName() {
        return bankName == null ? null : bankName.trim();
    }

    /**
     * 结构名、库名、前缀名 set方法.
     */
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    /**
     * 表名 get方法.
     */
    public String getTableName() {
        return tableName == null ? null : tableName.trim();
    }

    /**
     * 表名 set方法.
     */
    public void setTableName(String tableName) {
        this.tableName = tableName == null ? null : tableName.trim();
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

    /**
     * 数据源名称 get方法.
     */
    public String getSourceName() {
        return sourceName;
    }

    /**
     * 数据源名称 set方法.
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
