package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.MrJobTasksSchedule;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 任务泳道进度表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksScheduleService {

    Integer insert(MrJobTasksSchedule mrJobTasksSchedule);

    Integer update(Long id, MrJobTasksSchedule mrJobTasksSchedule);

    Integer delete(Long id);

    MrJobTasksSchedule selectById(Long id);

    Page<MrJobTasksSchedule> page(Page<MrJobTasksSchedule> page);

}
