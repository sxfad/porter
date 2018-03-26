/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.core.mapper.AlarmMapper;
import com.suixingpay.datas.manager.service.AlarmPluginService;
import com.suixingpay.datas.manager.service.AlarmService;
import com.suixingpay.datas.manager.service.AlarmUserService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 告警配置表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Autowired
    private AlarmPluginService alarmPluginService;

    @Autowired
    private AlarmUserService alarmUserService;

    @Override
    public Alarm selectFinallyOne() {
        Alarm alarm = alarmMapper.selectFinallyOne();
        if (alarm != null && alarm.getId() != null) {
            alarm.setAlarmPlugins(alarmPluginService.selectByAlarmId(alarm.getId()));
            alarm.setAlarmUsers(alarmUserService.selectByAlarmId(alarm.getId()));
            return alarm;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public Integer insert(Alarm alarm) {
        Integer i = alarmMapper.insert(alarm);
        alarmPluginService.insert(alarm);
        alarmUserService.insert(alarm);
        return i;
    }

    @Override
    public Integer update(Long id, Alarm alarm) {
        return alarmMapper.update(id, alarm);
    }

    @Override
    public Integer delete(Long id) {
        return alarmMapper.delete(id);
    }

    @Override
    public Alarm selectById(Long id) {
        return alarmMapper.selectById(id);
    }

    @Override
    public Page<Alarm> page(Page<Alarm> page) {
        Integer total = alarmMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(alarmMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public Integer insertSelective(Alarm alarm) {
        return alarmMapper.insertSelective(alarm);
    }

    @Override
    public Integer updateSelective(Long id, Alarm alarm) {
        return alarmMapper.updateSelective(id, alarm);
    }
}
