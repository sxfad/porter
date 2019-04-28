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

import cn.vbill.middleware.porter.manager.core.entity.DicControlTypePlugin;
import cn.vbill.middleware.porter.manager.core.mapper.DicControlTypePluginMapper;
import cn.vbill.middleware.porter.manager.service.DicControlTypePluginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作类型字典 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
@Service
public class DicControlTypePluginServiceImpl implements DicControlTypePluginService {

    @Autowired
    private DicControlTypePluginMapper dicControlTypePluginMapper;

    @Override
    public List<DicControlTypePlugin> findAll() {
        return dicControlTypePluginMapper.findAll();
    }

    @Override
    public List<DicControlTypePlugin> findByType(Integer type) {
        return dicControlTypePluginMapper.findByType(type);
    }


}
