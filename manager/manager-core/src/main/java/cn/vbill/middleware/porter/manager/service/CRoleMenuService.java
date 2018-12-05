package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.dto.CRoleMenuVo;
import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;

import java.util.List;

/**
 * 权限管理类
 *
 * @author: he_xin
 * @date: 2018-12-04 10:40:28
 * @version: V1.0
 * @review: he_xin/2018-12-04 10:40:28
 */
public interface CRoleMenuService {

    /**
     * 添加某一权限能访问的菜单
     *
     * @param cRoleMenuVoList
     */
    void insert(List<CRoleMenuVo> cRoleMenuVoList);

    /**
     * 回显权限和能访问的菜单
     *
     * @return
     */
    List<CRoleMenu> getRoleMenu();


}
