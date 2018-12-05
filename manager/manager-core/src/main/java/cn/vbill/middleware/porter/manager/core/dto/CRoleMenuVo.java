/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: hexin[he_xin@suixingpay.com]
 * @date: 2018年12月04日 15时01分
 * @Copyright 2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.core.dto;

import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;

import java.util.List;

/**
 * @Classname CRoleMenuVo
 * @Description 权限菜单
 * @Date 2018/12/4 15:01
 * @author hexin[he_xin@suixingpay.com]
 */
public class CRoleMenuVo {

    private String roleCode;

    private List<CRoleMenu> cRoleMenuList;

    public CRoleMenuVo(String roleCode, List<CRoleMenu> cRoleMenuList) {
        this.roleCode = roleCode;
        this.cRoleMenuList = cRoleMenuList;
    }

    public CRoleMenuVo() {
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public List<CRoleMenu> getcRoleMenuList() {
        return cRoleMenuList;
    }

    public void setcRoleMenuList(List<CRoleMenu> cRoleMenuList) {
        this.cRoleMenuList = cRoleMenuList;
    }
}
