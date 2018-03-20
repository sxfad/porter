package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.MrNodesSchedule;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 节点任务监控表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrNodesScheduleService {

    Integer insert(MrNodesSchedule mrNodesSchedule);

    Integer update(Long id, MrNodesSchedule mrNodesSchedule);

    Integer delete(Long id);

    MrNodesSchedule selectById(Long id);

    Page<MrNodesSchedule> page(Page<MrNodesSchedule> page, String ipAddress, String computerName);

}
