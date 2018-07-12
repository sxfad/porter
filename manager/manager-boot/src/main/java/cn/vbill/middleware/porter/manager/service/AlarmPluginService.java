package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

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

    List<AlarmPlugin> selectByAlarmId(Long alarmId);

    AlarmPlugin selectById(Long id);

    Page<AlarmPlugin> page(Page<AlarmPlugin> page);

    Integer insertSelective(AlarmPlugin alarmPlugin);

    Integer updateSelective(Long id, AlarmPlugin alarmPlugin);

}
