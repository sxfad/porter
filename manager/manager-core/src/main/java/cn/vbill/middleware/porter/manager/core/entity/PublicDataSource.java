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
import java.util.Date;

/**
 * 公共数据源配置表 实体Entity
 *
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
public class PublicDataSource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 数据源编码.
     */
    private String code;

    /**
     * 数据源名称.
     */
    private String name;

    /**
     * 数据源xml文本.
     */
    private String xmlText;

    /**
     * 数据源json文本.
     */
    private String jsonText;

    /**
     * 数据源说明.
     */
    private String declares;

    /**
     * 创建人.
     */
    private Long creator;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 类型.
     */
    private Integer type;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 推送状态
     */
    private Integer ispush;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 主键.get方法
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键.set方法
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 数据源编码.get方法
     */
    public String getCode() {
        return code;
    }

    /**
     * 数据源编码.set方法
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 数据源名称.get方法
     */
    public String getName() {
        return name;
    }

    /**
     * 数据源名称.set方法
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 数据源xml文本.get方法
     */
    public String getXmlText() {
        return xmlText;
    }

    /**
     * 数据源xml文本.set方法
     */
    public void setXmlText(String xmlText) {
        this.xmlText = xmlText;
    }

    /**
     * 数据源json文本.get方法
     */
    public String getJsonText() {
        return jsonText;
    }

    /**
     * 数据源json文本.set方法
     */
    public void setJsonText(String jsonText) {
        this.jsonText = jsonText;
    }

    /**
     * 数据源说明.get方法
     */
    public String getDeclares() {
        return declares;
    }

    /**
     * 数据源说明.set方法
     */
    public void setDeclares(String declares) {
        this.declares = declares;
    }

    /**
     * 创建人.get方法
     */
    public Long getCreator() {
        return creator;
    }

    /**
     * 创建人.set方法
     */
    public void setCreator(Long creator) {
        this.creator = creator;
    }

    /**
     * 创建时间.get方法
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间.set方法
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 状态.get方法
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态.set方法
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 类型.get方法
     */
    public Integer getType() {
        return type;
    }

    /**
     * 类型.set方法
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 是否作废.get方法
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废.set方法
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    public Integer getIspush() {
        return ispush;
    }

    public void setIspush(Integer ispush) {
        this.ispush = ispush;
    }

    /**
     * 备注.get方法
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 备注.set方法
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
