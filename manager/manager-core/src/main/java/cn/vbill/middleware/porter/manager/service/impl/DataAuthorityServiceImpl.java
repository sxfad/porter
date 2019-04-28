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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.vbill.middleware.porter.manager.core.dto.DataAuthorityVo;
import cn.vbill.middleware.porter.manager.core.dto.OwnerVo;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
import cn.vbill.middleware.porter.manager.core.enums.AuthorityBtnEnum;
import cn.vbill.middleware.porter.manager.core.mapper.DataAuthorityMapper;
import cn.vbill.middleware.porter.manager.service.DataAuthorityService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;

/**
 * 数据权限控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
@Service
public class DataAuthorityServiceImpl implements DataAuthorityService {

    private Logger log = LoggerFactory.getLogger(DataAuthorityServiceImpl.class);

    //顶配
    private static final String A0001 = "A0001";

    //普配
    private static final String A0002 = "A0002";

    //楼币
    private static final String A9999 = "A9999";

    @Autowired
    private DataAuthorityMapper dataAuthorityMapper;

    @Override
    public Integer insert(DataAuthority dataAuthority) {
        return dataAuthorityMapper.insert(dataAuthority);
    }

    @Override
    public Integer update(Long id, DataAuthority dataAuthority) {
        return dataAuthorityMapper.update(id, dataAuthority);
    }

    @Override
    public Integer delete(Long id) {
        return dataAuthorityMapper.delete(id);
    }

    @Override
    public DataAuthority selectById(Long id) {
        return dataAuthorityMapper.selectById(id);
    }

    @Override
    public Page<DataAuthority> page(Page<DataAuthority> page) {
        Integer total = dataAuthorityMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dataAuthorityMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<CUser> selectOwnersByObjectId(String objectName, Long objectId, Integer type) {
        return dataAuthorityMapper.selectOwnersByObjectId(objectName, objectId, type);
    }

    @Override
    public DataAuthorityVo dataAuthorityVo(String objectTable, Long objectId) {
        // 当前用户
        Long userId = RoleCheckContext.getUserIdHolder().getUserId();
        // 当前角色组
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        List<CUser> cusers = this.selectOwnersByObjectId(objectTable, objectId, 1);
        // 权限所有人
        OwnerVo owner = new OwnerVo((cusers == null || cusers.size() == 0) ? null : cusers.get(0), 1);
        // 权限共享者
        List<OwnerVo> shareOwner = userToVO(this.selectOwnersByObjectId(objectTable, objectId, 2), 2);
        // 展现按钮
        List<AuthorityBtnEnum> btns = this.selectBtns(objectTable, objectId, roleCode, userId);
        // 组装数据
        DataAuthorityVo vo = new DataAuthorityVo(userId, owner, shareOwner, btns);
        return vo;
    }

    private List<OwnerVo> userToVO(List<CUser> users, Integer authorityType) {
        List<OwnerVo> vo = new ArrayList<>();
        for (CUser user : users) {
            vo.add(new OwnerVo(user, authorityType));
        }
        return vo;
    }

    // 移交
    @Override
    public Boolean turnover(String objectTable, Long objectId, Long ownerId) {
        Boolean key = true;
        try {
            // 当前用户
            Long userId = RoleCheckContext.getUserIdHolder().getUserId();
            // 当前角色组
            String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
            // 先删除 delete objectTable objectId userId type=1
            Integer i = dataAuthorityMapper.deleteByMores(objectTable, objectId, userId, 1);
            if (i != 1) {
                log.warn("数据标识：[{}] 数据标号：[{}] 当前用户：[{}] 当前用户组：[{}] 不是数据权限所有者，特此告警！", objectTable, objectId, userId, roleCode);
            }
            // 再创建 insert objectTable objectId userId type=1
            Integer j = dataAuthorityMapper.insert(new DataAuthority(objectTable, objectId, 1, ownerId, userId, 1));
            if (j != 1) {
                log.error("数据标识：[{}] 数据标号：[{}] 移交用户：[{}] 移交数据权限失败，特此告警！", objectTable, objectId, ownerId);
            }
        } catch (Exception e) {
            key = false;
            log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 权限移交失败，记录关注！", objectTable, objectId, ownerId, e);
        }
        return key;
    }

    @Override
    public Boolean addShare(String objectTable, Long objectId, Long ownerId) {
        Boolean key = true;
        // 当前用户
        Long userId = RoleCheckContext.getUserIdHolder().getUserId();
        // 当前角色组
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        if (!A0001.equals(roleCode) && !A0002.equals(roleCode) && !A9999.equals(roleCode)) {
            log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
            return false;
        }
        if (A9999.equals(roleCode)) {
            DataAuthority dataAu = dataAuthorityMapper.selectOneByConditions(objectTable, objectId, 1, userId);
            if (dataAu == null) {
                log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
                return false;
            }
        }
        //增加共享者
        dataAuthorityMapper.insert(new DataAuthority(objectTable, objectId, 1, ownerId, userId, 2));
        return key;
    }

    @Override
    public Boolean delShare(String objectTable, Long objectId, Long ownerId) {
        Boolean key = true;
        // 当前用户
        Long userId = RoleCheckContext.getUserIdHolder().getUserId();
        // 当前角色组
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        if (!A0001.equals(roleCode) && !A0002.equals(roleCode) && !A9999.equals(roleCode)) {
            log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
            return false;
        }
        if (A9999.equals(roleCode)) {
            DataAuthority dataAu = dataAuthorityMapper.selectOneByConditions(objectTable, objectId, 1, userId);
            if (dataAu == null) {
                log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
                return false;
            }
        }
        //清空共享者
        dataAuthorityMapper.deleteByMores(objectTable, objectId, ownerId, 2);
        return key;
    }

    // 共享
    @Override
    public Boolean share(String objectTable, Long objectId, Long[] ownerIds) {
        Boolean key = true;
        // 当前用户
        Long userId = RoleCheckContext.getUserIdHolder().getUserId();
        // 当前角色组
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        try {
            if (!A0001.equals(roleCode) && !A0002.equals(roleCode) && !A9999.equals(roleCode)) {
                log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
                return false;
            }
            if (A9999.equals(roleCode)) {
                DataAuthority dataAu = dataAuthorityMapper.selectOneByConditions(objectTable, objectId, 1, userId);
                if (dataAu == null) {
                    log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 当前用户组：[{}] 非法操作，建议禁用此人账户！", objectTable, objectId, userId, roleCode);
                    return false;
                }
            }
            // 先清空共享者 delete objectTable objectId userId type=2
            dataAuthorityMapper.deleteByMores(objectTable, objectId, null, 2);
            // 再创建 insert objectTable objectId userId type=2
            for (Long ownerId : ownerIds) {
                dataAuthorityMapper.insert(new DataAuthority(objectTable, objectId, 1, ownerId, userId, 2));
            }
        } catch (Exception e) {
            key = false;
            log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 权限共享失败，记录关注！", objectTable, objectId, userId, e);
        }
        return key;
    }

    // 放弃
    @Override
    public Boolean waive(String objectTable, Long objectId) {
        Boolean key = true;
        try {
            // 当前用户
            Long userId = RoleCheckContext.getUserIdHolder().getUserId();
            // 直接删除 delete objectTable objectId userId
            dataAuthorityMapper.deleteByMores(objectTable, objectId, userId, null);
        } catch (Exception e) {
            key = false;
            log.error("数据标识：[{}] 数据标号：[{}] 登陆用户：[{}] 放弃权限失败，记录关注！", e);
        }
        return key;
    }

    @Override
    public List<AuthorityBtnEnum> selectBtns(String objectTable, Long objectId, String roleCode, Long userId) {
        List<AuthorityBtnEnum> btns = new ArrayList<>();
        if (A0001.equals(roleCode) || A0002.equals(roleCode)) {
            // 移交权限所有人按钮
            btns.add(AuthorityBtnEnum.TURNOVER);
            // 共享权限所有人设置按钮
            btns.add(AuthorityBtnEnum.SHARE);
        }
        if (A9999.equals(roleCode)) {
            DataAuthority daty = dataAuthorityMapper.selectOneByConditions(objectTable, objectId, 1, userId);
            if (daty == null) {
                btns = null;
            }
            switch (daty.getType()) {
                case 1:
                    // 移交权限所有人按钮
                    btns.add(AuthorityBtnEnum.TURNOVER);
                    // 共享权限所有人设置按钮
                    btns.add(AuthorityBtnEnum.SHARE);
                    // 放弃权限
                    btns.add(AuthorityBtnEnum.WAIVE);
                    break;
                case 2:
                    // 共享权限所有人设置按钮
                    btns.add(AuthorityBtnEnum.SHARE);
                    // 放弃权限
                    btns.add(AuthorityBtnEnum.WAIVE);
                    break;
                default:
                    btns = null;
                    break;
            }
        }
        return btns;
    }

}
