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

import cn.vbill.middleware.porter.manager.core.mapper.DicAlarmPluginMapper;
import cn.vbill.middleware.porter.manager.core.entity.DicAlarmPlugin;
import cn.vbill.middleware.porter.manager.service.DicAlarmPluginService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警配置策略字典表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class DicAlarmPluginServiceImpl implements DicAlarmPluginService {

    @Autowired
    private DicAlarmPluginMapper dicAlarmPluginMapper;

    @Override
    public Integer insert(DicAlarmPlugin dicAlarmPlugin) {
        return dicAlarmPluginMapper.insert(dicAlarmPlugin);
    }

    @Override
    public Integer update(Long id, DicAlarmPlugin dicAlarmPlugin) {
        return dicAlarmPluginMapper.update(id, dicAlarmPlugin);
    }

    @Override
    public Integer delete(Long id) {
        return dicAlarmPluginMapper.delete(id);
    }

    @Override
    public DicAlarmPlugin selectById(Long id) {
        return dicAlarmPluginMapper.selectById(id);
    }

    @Override
    public Page<DicAlarmPlugin> page(Page<DicAlarmPlugin> page) {
        Integer total = dicAlarmPluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(dicAlarmPluginMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<DicAlarmPlugin> findByAlertType(String alertType) {
        return dicAlarmPluginMapper.findByAlertType(alertType);
    }


}
