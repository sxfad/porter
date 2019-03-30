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

import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
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
}
