package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.DicAlarmPlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

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

    List<DicAlarmPlugin> findByAlertType(String alertType);

}
