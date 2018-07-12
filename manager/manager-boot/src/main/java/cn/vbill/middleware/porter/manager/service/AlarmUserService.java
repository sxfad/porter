package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmUser;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 告警用户关联表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface AlarmUserService {

    void insert(Alarm alarm);

    Integer insert(AlarmUser alarmUser);

    Integer update(Long id, AlarmUser alarmUser);

    Integer delete(Long id);

    AlarmUser selectById(Long id);

    Page<AlarmUser> page(Page<AlarmUser> page);

    List<AlarmUser> selectByAlarmId(Long alarmId);
}
