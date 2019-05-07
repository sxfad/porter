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

import java.util.List;

import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.core.mapper.CUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 登陆用户表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CUserServiceImpl implements CUserService {

    @Autowired
    private CUserMapper cuserMapper;

    @Override
    public Integer insert(CUser cuser) {
        return cuserMapper.insert(cuser);
    }

    @Override
    public Integer update(Long id, CUser cuser) {
        return cuserMapper.updateSelective(id, cuser);
    }

    @Override
    public Integer delete(Long id) {
        return cuserMapper.delete(id);
    }

    @Override
    public CUser selectById(Long id) {
        return cuserMapper.selectById(id);
    }

    @Override
    public CUser selectByNameAndpasswd(String loginName, String passwd) {
        return cuserMapper.selectByNameAndpasswd(loginName, passwd);
    }

    @Override
    public Page<CUser> page(Page<CUser> page) {
        Integer total = cuserMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(cuserMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<CUser> list() {
        return cuserMapper.list();
    }

    @Override
    public List<CUser> selectByJobTasksId(Long jobTasksId) {
        return cuserMapper.selectByJobTasksId(jobTasksId);
    }

    @Override
    public List<CUser> selectByAlarmId(Long alarmId) {
        return cuserMapper.selectByAlarmId(alarmId);
    }

    @Override
    public boolean findByNameOrEmail(String loginname, String email) {
        boolean flag = false;
        CUser cUser = cuserMapper.findByNameOrEmail(loginname, email);
        if (cUser == null) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Integer updateState(Long id, Integer state) {
        return cuserMapper.updateState(id, state);
    }

    @Override
    public Integer register(CUser cUser) {
        // 新注册的用户状态是启用 1为正常 0为禁止登陆 -1为删除
        cUser.setState(1);
        // 新注册用户为注册用户身份
        cUser.setRoleCode("A9999");
        return cuserMapper.register(cUser);
    }

    @Override
    public Long checkLoginName(String loginName) {
        return cuserMapper.checkLoginName(loginName);
    }

    @Override
    public List<CUser> selectOwnersByJobId(Long jobId, Integer type) {
        if (null == jobId) {
            return null;
        }
        return cuserMapper.selectOwnersByJobId(jobId, type);
    }

    @Override
    public List<CUser> findRegister() {
        return cuserMapper.findRegister();
    }

    @Override
    public List<CUser> selectOwnersByNodeId(Long nodeId, Integer type) {
        if (null == nodeId) {
            return null;
        }
        return cuserMapper.selectOwnersByNodeId(nodeId, type);
    }

}
