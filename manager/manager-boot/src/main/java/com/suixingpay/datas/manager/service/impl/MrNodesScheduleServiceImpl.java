/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.MrNodesSchedule;
import com.suixingpay.datas.manager.core.mapper.MrNodesScheduleMapper;
import com.suixingpay.datas.manager.service.MrNodesScheduleService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 节点任务监控表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrNodesScheduleServiceImpl implements MrNodesScheduleService {

    @Autowired
    private MrNodesScheduleMapper mrNodesScheduleMapper;

    @Override
    public Integer insert(MrNodesSchedule mrNodesSchedule) {
        return mrNodesScheduleMapper.insert(mrNodesSchedule);
    }

    @Override
    public Integer update(Long id, MrNodesSchedule mrNodesSchedule) {
        return mrNodesScheduleMapper.update(id, mrNodesSchedule);
    }

    @Override
    public Integer delete(Long id) {
        return mrNodesScheduleMapper.delete(id);
    }

    @Override
    public MrNodesSchedule selectById(Long id) {
        return mrNodesScheduleMapper.selectById(id);
    }

    @Override
    public Page<MrNodesSchedule> page(Page<MrNodesSchedule> page) {
        Integer total = mrNodesScheduleMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrNodesScheduleMapper.page(page, 1));
        }
        return page;
    }
}
