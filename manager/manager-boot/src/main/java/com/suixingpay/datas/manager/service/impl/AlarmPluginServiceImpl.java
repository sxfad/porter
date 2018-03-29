/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.core.mapper.AlarmPluginMapper;
import com.suixingpay.datas.manager.service.AlarmPluginService;
import com.suixingpay.datas.manager.web.page.Page;
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
