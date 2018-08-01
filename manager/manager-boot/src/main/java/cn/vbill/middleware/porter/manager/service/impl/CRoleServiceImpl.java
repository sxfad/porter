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

import cn.vbill.middleware.porter.manager.core.mapper.CRoleMapper;
import cn.vbill.middleware.porter.manager.service.CRoleService;
import cn.vbill.middleware.porter.manager.core.entity.CRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CRoleServiceImpl implements CRoleService {

    @Autowired
    private CRoleMapper croleMapper;

    @Override
    public List<CRole> findAll() {
        return croleMapper.findAll();
    }

    @Override
    public List<CRole> findList() {
        return croleMapper.findList();
    }
}
