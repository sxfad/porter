/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-08 10:46:01  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.core.mapper.AlarmMapper;
import com.suixingpay.datas.manager.service.AlarmService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 告警配置表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class AlarmServiceImpl implements AlarmService {

    @Autowired
    private AlarmMapper alarmMapper;

    @Override
    public Integer insert(Alarm alarm) {
        return alarmMapper.insert(alarm);
    }

    @Override
    public Integer update(Long id, Alarm alarm) {
        return alarmMapper.update(id, alarm);
    }

    @Override
    public Integer delete(Long id) {
        return alarmMapper.delete(id);
    }

    @Override
    public Alarm selectById(Long id) {
        return alarmMapper.selectById(id);
    }

    @Override
    public Page<Alarm> page(Page<Alarm> page) {
        Integer total = alarmMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(alarmMapper.page(page, 1));
        }
        return page;
    }
}
