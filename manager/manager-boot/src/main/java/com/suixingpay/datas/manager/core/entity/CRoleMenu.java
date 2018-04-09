/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2018-04-09 16:45:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.core.entity;

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
