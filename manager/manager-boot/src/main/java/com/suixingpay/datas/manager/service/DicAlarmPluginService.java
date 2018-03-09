package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.DicAlarmPlugin;
import com.suixingpay.datas.manager.web.page.Page;


/**
 * 告警配置策略字典表 服务接口类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */

public interface DicAlarmPluginService {

    Integer insert(DicAlarmPlugin dicAlarmPlugin);

    Integer update(Long id, DicAlarmPlugin dicAlarmPlugin);

    Integer delete(Long id);

    DicAlarmPlugin selectById(Long id);

    Page<DicAlarmPlugin> page(Page<DicAlarmPlugin> page);

}
