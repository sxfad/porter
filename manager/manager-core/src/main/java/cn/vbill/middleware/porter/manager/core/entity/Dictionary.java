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
 * 数据字典表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class Dictionary implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 编码.
     */
    private String code;

    /**
     * 名称.
     */
    private String name;

    /**
     * 父类编码.
     */
    private String parentcode;

    /**
     * 树形等级.
     */
    private Integer level;

    /**
     * 字典类别.
     */
    private String dictype;

    /**
     * 数据状态1正常-1作废0警告.
     */
    private Integer state;

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
     * 编码 get方法.
     */
    public String getCode() {
        return code == null ? null : code.trim();
    }

    /**
     * 编码 set方法.
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 名称 get方法.
     */
    public String getName() {
        return name == null ? null : name.trim();
    }

    /**
     * 名称 set方法.
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 父类编码 get方法.
     */
    public String getParentcode() {
        return parentcode == null ? null : parentcode.trim();
    }

    /**
     * 父类编码 set方法.
     */
    public void setParentcode(String parentcode) {
        this.parentcode = parentcode == null ? null : parentcode.trim();
    }

    /**
     * 树形等级 get方法.
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 树形等级 set方法.
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 字典类别 get方法.
     */
    public String getDictype() {
        return dictype == null ? null : dictype.trim();
    }

    /**
     * 字典类别 set方法.
     */
    public void setDictype(String dictype) {
        this.dictype = dictype == null ? null : dictype.trim();
    }

    /**
     * 数据状态1正常-1作废0警告 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 数据状态1正常-1作废0警告 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
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
