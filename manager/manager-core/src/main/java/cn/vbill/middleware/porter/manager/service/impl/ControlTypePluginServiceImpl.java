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

import cn.vbill.middleware.porter.manager.core.entity.ControlTypePlugin;
import cn.vbill.middleware.porter.manager.core.mapper.ControlTypePluginMapper;
import cn.vbill.middleware.porter.manager.service.ControlTypePluginService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 操作类型字典 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
@Service
public class ControlTypePluginServiceImpl implements ControlTypePluginService {

    @Autowired
    private ControlTypePluginMapper controlTypePluginMapper;

    @Override
    public Integer insert(ControlTypePlugin controlTypePlugin) {
        return controlTypePluginMapper.insert(controlTypePlugin);
    }

    @Override
    public Integer update(Long id, ControlTypePlugin controlTypePlugin) {
        return controlTypePluginMapper.update(id, controlTypePlugin);
    }

    @Override
    public Integer delete(Long id) {
        return controlTypePluginMapper.delete(id);
    }

    @Override
    public ControlTypePlugin selectById(Long id) {
        return controlTypePluginMapper.selectById(id);
    }

    @Override
    public Page<ControlTypePlugin> page(Page<ControlTypePlugin> page) {
        Integer total = controlTypePluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(controlTypePluginMapper.page(page, 1));
        }
        return page;
    }

}
