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

import cn.vbill.middleware.porter.manager.core.mapper.DicDataSourcePluginMapper;
import cn.vbill.middleware.porter.manager.core.entity.DicDataSourcePlugin;
import cn.vbill.middleware.porter.manager.service.DicDataSourcePluginService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据源信息字典表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Service
public class DicDataSourcePluginServiceImpl implements DicDataSourcePluginService {

    @Autowired
    private DicDataSourcePluginMapper dicDataSourcePluginMapper;

    @Override
    public Integer insert(DicDataSourcePlugin dicDataSourcePlugin) {
        return dicDataSourcePluginMapper.insert(dicDataSourcePlugin);
    }

    @Override
    public Integer update(Long id, DicDataSourcePlugin dicDataSourcePlugin) {
        return dicDataSourcePluginMapper.update(id, dicDataSourcePlugin);
    }

    @Override
    public Integer delete(Long id) {
        return dicDataSourcePluginMapper.delete(id);
    }

    @Override
    public DicDataSourcePlugin selectById(Long id) {
        return dicDataSourcePluginMapper.selectById(id);
    }

    @Override
    public Page<DicDataSourcePlugin> page(Page<DicDataSourcePlugin> page) {
        Integer total = dicDataSourcePluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dicDataSourcePluginMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<DicDataSourcePlugin> findByType(String sourceType) {
        return dicDataSourcePluginMapper.findByType(sourceType);
    }
}
