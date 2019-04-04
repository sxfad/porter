/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */
package cn.vbill.middleware.porter.manager.core.entity;

import java.io.Serializable;

/**
 * 操作类型字典 实体Entity
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
public class DicControlTypePlugin implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 告警策略.
     */
    private String alertType;

    /**
     * 页面字段名称.
     */
    private String fieldName;

    /**
     * 字段实际名.
     */
    private String fieldCode;

    /**
     * 页面展示顺序.
     */
    private Integer fieldOrder;

    /**
     * 页面字段类型.
     */
    private String fieldType;

    /**
     * 页面字段类型对应字典.
     */
    private String fieldTypeKey;

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
     * 主键.get
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键.set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 告警策略.get
     */
    public String getAlertType() {
        return alertType;
    }

    /**
     * 告警策略.set
     */
    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    /**
     * 页面字段名称.get
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * 页面字段名称.set
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * 字段实际名.get
     */
    public String getFieldCode() {
        return fieldCode;
    }

    /**
     * 字段实际名.set
     */
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode;
    }

    /**
     * 页面展示顺序.get
     */
    public Integer getFieldOrder() {
        return fieldOrder;
    }

    /**
     * 页面展示顺序.set
     */
    public void setFieldOrder(Integer fieldOrder) {
        this.fieldOrder = fieldOrder;
    }

    /**
     * 页面字段类型.get
     */
    public String getFieldType() {
        return fieldType;
    }

    /**
     * 页面字段类型.set
     */
    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * 页面字段类型对应字典.get
     */
    public String getFieldTypeKey() {
        return fieldTypeKey;
    }

    /**
     * 页面字段类型对应字典.set
     */
    public void setFieldTypeKey(String fieldTypeKey) {
        this.fieldTypeKey = fieldTypeKey;
    }

    /**
     * 状态.get
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态.set
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 是否作废.get
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废.set
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /**
     * 备注.get
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注.set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
