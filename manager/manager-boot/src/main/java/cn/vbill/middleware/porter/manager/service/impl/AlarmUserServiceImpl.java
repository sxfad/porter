/**
 *
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmUser;
import cn.vbill.middleware.porter.manager.core.mapper.AlarmUserMapper;
import cn.vbill.middleware.porter.manager.service.AlarmUserService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 告警用户关联表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class AlarmUserServiceImpl implements AlarmUserService {

    @Autowired
    private AlarmUserMapper alarmUserMapper;

    @Override
    public void insert(Alarm alarm) {
        alarmUserMapper.deleteByAlarmId(alarm.getId());
        for (AlarmUser alarmUser : alarm.getAlarmUsers()) {
            alarmUser.setAlarmId(alarm.getId());
            insert(alarmUser);
        }
    }

    @Override
    public Integer insert(AlarmUser alarmUser) {
        return alarmUserMapper.insert(alarmUser);
    }

    @Override
    public Integer update(Long id, AlarmUser alarmUser) {
        return alarmUserMapper.update(id, alarmUser);
    }

    @Override
    public Integer delete(Long id) {
        return alarmUserMapper.delete(id);
    }

    @Override
    public AlarmUser selectById(Long id) {
        return alarmUserMapper.selectById(id);
    }

    @Override
    public Page<AlarmUser> page(Page<AlarmUser> page) {
        Integer total = alarmUserMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(alarmUserMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<AlarmUser> selectByAlarmId(Long alarmId) {
        return alarmUserMapper.selectByAlarmId(alarmId);
    }
}
