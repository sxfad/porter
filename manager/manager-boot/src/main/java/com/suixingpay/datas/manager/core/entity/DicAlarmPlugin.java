package com.suixingpay.datas.manager.core.entity;

/**
 * 告警配置策略字典表 实体Entity
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public class DicAlarmPlugin implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /** 主键. */
    private Long id;

    /** 告警策略. */
    private String alertType;

    /** 字段实际名. */
    private String fieldCode;

    /** 字段展示名. */
    private String fieldName;

    /** 字段页面顺序. */
    private Integer fieldOrder;

    /** 状态. */
    private Integer state;

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

    /** 告警策略 get方法. */
    public String getAlertType() {
        return alertType == null ? null : alertType.trim();
    }

    /** 告警策略 set方法. */
    public void setAlertType(String alertType) {
        this.alertType = alertType == null ? null : alertType.trim();
    }

    public String getFieldCode() {
        return fieldCode;
    }

    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getFieldOrder() {
        return fieldOrder;
    }

    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    /** 状态 get方法. */
    public Integer getState() {
        return state;
    }

    /** 状态 set方法. */
    public void setState(Integer state) {
        this.state = state;
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
