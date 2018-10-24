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

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin;
import cn.vbill.middleware.porter.manager.core.mapper.AlarmPluginMapper;
import cn.vbill.middleware.porter.manager.service.AlarmPluginService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警配置策略内容表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class AlarmPluginServiceImpl implements AlarmPluginService {

    @Autowired
    private AlarmPluginMapper alarmPluginMapper;

    @Override
    public void insert(Alarm alarm) {
        for (AlarmPlugin alarmPlugin : alarm.getAlarmPlugins()) {
            alarmPlugin.setAlarmId(alarm.getId());
            alarmPlugin.setAlarmType(alarm.getAlarmType().getCode());
            insert(alarmPlugin);
        }
    }

    @Override
    public List<AlarmPlugin> selectByAlarmId(Long alarmId) {
        return alarmPluginMapper.selectByAlarmId(alarmId);
    }

    @Override
    public Integer insert(AlarmPlugin alarmPlugin) {
        return alarmPluginMapper.insert(alarmPlugin);
    }

    @Override
    public Integer update(Long id, AlarmPlugin alarmPlugin) {
        return alarmPluginMapper.update(id, alarmPlugin);
    }

    @Override
    public Integer delete(Long id) {
        return alarmPluginMapper.delete(id);
    }

    @Override
    public AlarmPlugin selectById(Long id) {
        return alarmPluginMapper.selectById(id);
    }

    @Override
    public Page<AlarmPlugin> page(Page<AlarmPlugin> page) {
        Integer total = alarmPluginMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(alarmPluginMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public Integer insertSelective(AlarmPlugin alarmPlugin) {
        return alarmPluginMapper.insertSelective(alarmPlugin);
    }

    @Override
    public Integer updateSelective(Long id, AlarmPlugin alarmPlugin) {
        return alarmPluginMapper.updateSelective(id, alarmPlugin);
    }

}
