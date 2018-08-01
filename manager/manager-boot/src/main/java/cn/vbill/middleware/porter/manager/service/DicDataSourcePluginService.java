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

import cn.vbill.middleware.porter.manager.core.entity.DicDataSourcePlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 数据源信息字典表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DicDataSourcePluginService {

    Integer insert(DicDataSourcePlugin dicDataSourcePlugin);

    Integer update(Long id, DicDataSourcePlugin dicDataSourcePlugin);

    Integer delete(Long id);

    DicDataSourcePlugin selectById(Long id);

    Page<DicDataSourcePlugin> page(Page<DicDataSourcePlugin> page);

    List<DicDataSourcePlugin> findByType(String sourceType);
}
