/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.manager.core.entity.MrJobTasksSchedule;
import com.suixingpay.datas.manager.core.mapper.MrJobTasksScheduleMapper;
import com.suixingpay.datas.manager.service.MrJobTasksScheduleService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 任务泳道进度表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrJobTasksScheduleServiceImpl implements MrJobTasksScheduleService {

    private ConcurrentHashMap<String, Object> map = new ConcurrentHashMap<>(128);

    @Autowired
    private MrJobTasksScheduleMapper mrJobTasksScheduleMapper;

    @Override
    public Integer insert(MrJobTasksSchedule mrJobTasksSchedule) {
        return mrJobTasksScheduleMapper.insert(mrJobTasksSchedule);
    }

    @Override
    public Integer update(Long id, MrJobTasksSchedule mrJobTasksSchedule) {
        return mrJobTasksScheduleMapper.update(id, mrJobTasksSchedule);
    }

    @Override
    public Integer delete(Long id) {
        return mrJobTasksScheduleMapper.delete(id);
    }

    @Override
    public MrJobTasksSchedule selectById(Long id) {
        return mrJobTasksScheduleMapper.selectById(id);
    }

    @Override
    public Page<MrJobTasksSchedule> page(Page<MrJobTasksSchedule> page) {
        Integer total = mrJobTasksScheduleMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(mrJobTasksScheduleMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void dealDTaskStat(DTaskStat stat) {
        MrJobTasksSchedule mrJobTasksSchedule = new MrJobTasksSchedule(stat);
        String jobId = mrJobTasksSchedule.getJobId();
        String swimlaneId = mrJobTasksSchedule.getSwimlaneId();
        String key = jobId + swimlaneId;
        Object lock = map.get(key);
        if (null == lock) {
            Object tmp = new Object();
            Object old = map.putIfAbsent(key, tmp);
            if (null != old) {
                lock = old;
            } else {
                lock = tmp;
            }
        }
        try {
            synchronized (lock) {
                dealDTaskStatSync(jobId, swimlaneId, mrJobTasksSchedule);
            }
        } finally {
            map.remove(key);
        }
    }

    @Override
    public List<MrJobTasksSchedule> selectSwimlaneByJobId(String jobId) {
        return mrJobTasksScheduleMapper.selectSwimlaneByJobId(jobId);
    }

    @Override
    public List<MrJobTasksSchedule> list(String jobId, String heartBeatBeginDate, String heartBeatEndDate) {
        return mrJobTasksScheduleMapper.list(jobId, heartBeatBeginDate, heartBeatEndDate);
    }

    private void dealDTaskStatSync(String jobId, String swimlaneId, MrJobTasksSchedule mrJobTasksSchedule) {
        MrJobTasksSchedule old = mrJobTasksScheduleMapper.selectByJobIdAndSwimlaneId(jobId, swimlaneId);
        if (old == null || old.getId() == null) {
            mrJobTasksScheduleMapper.insert(mrJobTasksSchedule);
        }else {
            mrJobTasksSchedule.setId(old.getId());
            mrJobTasksScheduleMapper.updateSelective(old.getId(), mrJobTasksSchedule);
        }
    }
}
