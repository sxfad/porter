package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 告警配置策略内容表 服务接口类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmPluginService {

    void insert(Alarm alarm);

    Integer insert(AlarmPlugin alarmPlugin);

    Integer update(Long id, AlarmPlugin alarmPlugin);

    Integer delete(Long id);

    AlarmPlugin selectById(Long id);

    Page<AlarmPlugin> page(Page<AlarmPlugin> page);

    Integer insertSelective(AlarmPlugin alarmPlugin);

    Integer updateSelective(Long id, AlarmPlugin alarmPlugin);

}
