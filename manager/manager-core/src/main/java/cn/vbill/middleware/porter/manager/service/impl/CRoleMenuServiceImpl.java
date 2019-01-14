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
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.dto.CRoleMenuVo;
import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;
import cn.vbill.middleware.porter.manager.core.mapper.CRoleMenuMapper;
import cn.vbill.middleware.porter.manager.service.CRoleMenuService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        for (CRoleMenuVo cRoleMenuVo : cRoleMenuVoList) {
            // 获取权限编号并删除以前的记录
            String roleCode = cRoleMenuVo.getRoleCode();
            cRoleMenuMapper.delete(roleCode);
            // 新增某一权限所能访问的菜单
            List<CRoleMenu> cRoleMenuList = cRoleMenuVo.getcRoleMenuList();
            if (cRoleMenuList != null && cRoleMenuList.size() > 0) {
                cRoleMenuMapper.insert(cRoleMenuList);
            }
        }
    }

    @Override
    public List<CRoleMenu> getRoleMenu() {
        return cRoleMenuMapper.getRoleMenu();
    }
}
