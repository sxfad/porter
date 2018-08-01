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

/**
 * 数据源信息关联表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public class DataSourcePlugin implements java.io.Serializable {

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
     * 页面字段名称.
     */
    private String fieldName;

    /**
     * 字段code.
     */
    private String fieldCode;

    /**
     * 页面传入的实际值.
     */
    private String fieldValue;

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
     * 页面字段名称 get方法.
     */
    public String getFieldName() {
        return fieldName == null ? null : fieldName.trim();
    }

    /**
     * 页面字段名称 set方法.
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName == null ? null : fieldName.trim();
    }

    /**
     * 字段code get方法.
     */
    public String getFieldCode() {
        return fieldCode == null ? null : fieldCode.trim();
    }

    /**
     * 字段code set方法.
     */
    public void setFieldCode(String fieldCode) {
        this.fieldCode = fieldCode == null ? null : fieldCode.trim();
    }

    /**
     * 页面传入的实际值 get方法.
     */
    public String getFieldValue() {
        return fieldValue == null ? null : fieldValue.trim();
    }

    /**
     * 页面传入的实际值 set方法.
     */
    public void setFieldValue(String fieldValue) {
        this.fieldValue = fieldValue == null ? null : fieldValue.trim();
    }

}
