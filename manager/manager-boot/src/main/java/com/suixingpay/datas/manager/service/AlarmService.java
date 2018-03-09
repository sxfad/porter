package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 告警配置表 服务接口类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmService {

    Integer insert(Alarm alarm);

    Integer update(Long id, Alarm alarm);

    Integer delete(Long id);

    Alarm selectById(Long id);

    Page<Alarm> page(Page<Alarm> page);
}
