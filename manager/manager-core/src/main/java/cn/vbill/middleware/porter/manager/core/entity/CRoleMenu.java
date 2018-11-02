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
 * 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-04-09 16:45:22
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-09 16:45:22
 */
public class CRoleMenu implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 角色id.
     */
    private String roleCode;

    /**
     * 菜单code.
     */
    private String menuCode;

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
     * 角色id get方法.
     */
    public String getRoleCode() {
        return roleCode == null ? null : roleCode.trim();
    }

    /**
     * 角色id set方法.
     */
    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode == null ? null : roleCode.trim();
    }

    /**
     * 菜单code get方法.
     */
    public String getMenuCode() {
        return menuCode == null ? null : menuCode.trim();
    }

    /**
     * 菜单code set方法.
     */
    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode == null ? null : menuCode.trim();
    }

}
