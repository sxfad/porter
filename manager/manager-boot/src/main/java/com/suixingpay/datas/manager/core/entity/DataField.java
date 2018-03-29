package com.suixingpay.datas.manager.core.entity;

import java.util.Date;

/**
 * 数据字段对应表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class DataField implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 数据表id.
     */
    private Long tableId;

    /**
     * 字段名称.
     */
    private String fieldName;

    /**
     * 字段类型.
     */
    private String fieldType;

    /**
     * 字段注释.
     */
    private String fieldComment;

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
     * 数据表id get方法.
     */
    public Long getTableId() {
        return tableId;
    }

    /**
     * 数据表id set方法.
     */
    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    /**
     * 字段名称 get方法.
     */
    public String getFieldName() {
        return fieldName == null ? null : fieldName.trim();
    }

    /**
     * 字段名称 set方法.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    /**
     * 字段类型 get方法.
     */
    public String getFieldType() {
        return fieldType == null ? null : fieldType.trim();
    }

    /**
     * 字段类型 set方法.
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType == null ? null : fieldType.trim();
    }

    /**
     * 字段注释 get方法.
     */
    public String getFieldComment() {
        return fieldComment == null ? null : fieldComment.trim();
    }

    /**
     * 字段注释 set方法.
     */
    public void setFieldComment(String fieldComment) {
        this.fieldComment = fieldComment == null ? null : fieldComment.trim();
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

}
