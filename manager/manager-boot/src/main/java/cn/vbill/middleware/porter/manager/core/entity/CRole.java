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
 * 角色表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class CRole implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 角色编号.
     */
    private String roleCode;

    /**
     * 角色名称.
     */
    private String roleName;

    /**
     * 角色排序.
     */
    private Integer sort;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 类型.
     */
    private Integer type;

    /**
     * 状态.
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
     * 角色编号 get方法.
     */
    public String getRoleCode() {
        return roleCode == null ? null : roleCode.trim();
    }

    /**
     * 角色编号 set方法.
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * 角色名称 get方法.
     */
    public String getRoleName() {
        return roleName == null ? null : roleName.trim();
    }

    /**
     * 角色名称 set方法.
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName == null ? null : roleName.trim();
    }

    /**
     * 角色排序 get方法.
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 角色排序 set方法.
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
     * 类型 get方法.
     */
    public Integer getType() {
        return type;
    }

    /**
     * 类型 set方法.
     */
    public void setType(Integer type) {
        this.type = type;
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
