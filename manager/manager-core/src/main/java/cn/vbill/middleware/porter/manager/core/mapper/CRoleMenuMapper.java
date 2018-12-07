package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CRoleMenuMapper {

    /**
     * 添加权限所能访问的菜单
     *
     * @author he_xin
     * @param cRoleMenuList
     */
    Integer insert(List<CRoleMenu> cRoleMenuList);

    /**
     * 回显权限和能访问的菜单
     *
     * @author he_xin
     * @return
     */
    List<CRoleMenu> getRoleMenu();

    /**
     * 删除之前权限能够访问的菜单
     *
     * @author he_xin
     * @param roleCode
     */
    void delete(@Param("roleCode") String roleCode);

}
