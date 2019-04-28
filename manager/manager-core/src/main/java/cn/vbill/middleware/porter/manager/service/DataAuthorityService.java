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
package cn.vbill.middleware.porter.manager.service;

import java.util.List;

import cn.vbill.middleware.porter.manager.core.dto.DataAuthorityVo;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
import cn.vbill.middleware.porter.manager.core.enums.AuthorityBtnEnum;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 数据权限控制表 服务接口类
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
public interface DataAuthorityService {

    /**
     * 新增
     * 
     * @param dataAuthority
     */
    Integer insert(DataAuthority dataAuthority);

    /**
     * 修改
     * 
     * @param id,
     * @param dataAuthority
     */
    Integer update(Long id, DataAuthority dataAuthority);

    /**
     * 删除
     * 
     * @param id
     */
    Integer delete(Long id);

    /**
     * 查询详情
     * 
     * @param id
     */
    DataAuthority selectById(Long id);

    /**
     * 分页查询
     * 
     * @param page
     */
    Page<DataAuthority> page(Page<DataAuthority> page);

    /**
     * 查询与ObjectId有关的权限所有者用户(type=null查询全部 type=1查询所有者 type=2查询共享者)
     * 
     * @param ObjectName
     *            对象标识
     * @param ObjectId
     *            对象id
     * @param type
     *            类型
     * @return
     */
    List<CUser> selectOwnersByObjectId(String objectName, Long objectId, Integer type);

    /**
     * 权限页面数据vo
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    DataAuthorityVo dataAuthorityVo(String objectTable, Long objectId);

    /**
     * 移交权限
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    Boolean turnover(String objectTable, Long objectId, Long ownerId);

    /**
     * add共享权限
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    Boolean addShare(String objectTable, Long objectId, Long ownerId);

    /**
     * del共享权限
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    Boolean delShare(String objectTable, Long objectId, Long ownerId);

    /**
     * 共享权限
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    Boolean share(String objectTable, Long objectId, Long[] ownerIds);

    /**
     * 放弃权限
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    Boolean waive(String objectTable, Long objectId);

    /**
     * 
     * @param objectTable
     * @param objectId
     * @return
     */
    List<AuthorityBtnEnum> selectBtns(String objectTable, Long objectId, String roleCode, Long userId);
}
