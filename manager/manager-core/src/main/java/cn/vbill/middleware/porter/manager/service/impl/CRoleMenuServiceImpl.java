/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: hexin[he_xin@suixingpay.com]
 * @date: 2018年12月04日 10时27分
 * @Copyright 2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.dto.CRoleMenuVo;
import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;
import cn.vbill.middleware.porter.manager.core.mapper.CRoleMenuMapper;
import cn.vbill.middleware.porter.manager.service.CRoleMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Classname CRoleMenuServiceImpl
 * @Description 角色管理
 * @Date 2018/12/4 10:27
 * @author hexin[he_xin@suixingpay.com]
 */
@Service
public class CRoleMenuServiceImpl implements CRoleMenuService {

    @Autowired
    private CRoleMenuMapper cRoleMenuMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(List<CRoleMenuVo> cRoleMenuVoList) {

        for(CRoleMenuVo cRoleMenuVo : cRoleMenuVoList){
            // 获取权限编号并删除以前的记录
            String roleCode = cRoleMenuVo.getRoleCode();
            cRoleMenuMapper.delete(roleCode);
            // 新增某一权限所能访问的菜单
            List<CRoleMenu> cRoleMenuList = cRoleMenuVo.getcRoleMenuList();
            if(cRoleMenuList != null && cRoleMenuList.size() > 0){
                cRoleMenuMapper.insert(cRoleMenuList);
            }
        }
    }

    @Override
    public List<CRoleMenu> getRoleMenu() {
        return cRoleMenuMapper.getRoleMenu();
    }
}
