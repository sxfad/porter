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

import cn.vbill.middleware.porter.common.config.PublicSourceConfig;
import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 公共数据源配置表 服务接口类
 *
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
public interface PublicDataSourceService {

    /**
     * 新增
     * 
     * @param publicDataSource
     * @return
     */
    Integer insert(PublicDataSource publicDataSource);

    /**
     * 修改
     * 
     * @param id
     * @param publicDataSource
     * @return
     */
    Integer update(Long id, PublicDataSource publicDataSource);

    /**
     * 作废
     * 
     * @param id
     * @return
     */
    Integer updateCancel(Long id);

    /**
     * 修改推送状态
     * 
     * @param id
     * @param ispush
     * @return
     */
    Integer updatePush(Long id, Integer ispush);

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 查找
     * 
     * @param id
     * @return
     */
    PublicDataSource selectById(Long id);

    /**
     * 分页
     * 
     * @param page
     * @return
     */
    Page<PublicDataSource> page(Page<PublicDataSource> page, Long id, String code, String name, String ipsite);

    /**
     * 解析配置任务
     * 
     * @param xmlTextStr
     * @return
     */
    PublicSourceConfig dealxml(String xmlTextStr);
}
