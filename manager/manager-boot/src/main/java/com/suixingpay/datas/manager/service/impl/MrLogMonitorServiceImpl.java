/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-07 13:40:30  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.MrLogMonitor;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.MrLogMonitorMapper;
import com.suixingpay.datas.manager.service.MrLogMonitorService;

/**
 * 日志监控信息表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class MrLogMonitorServiceImpl implements MrLogMonitorService {

    @Autowired
    private MrLogMonitorMapper mrLogMonitorMapper;

    @Override
    public Integer insert(MrLogMonitor mrLogMonitor) {
        return mrLogMonitorMapper.insert(mrLogMonitor);
    }

    @Override
    public Integer update(Long id, MrLogMonitor mrLogMonitor) {
        return mrLogMonitorMapper.update(id, mrLogMonitor);
    }

    @Override
    public Integer delete(Long id) {
        return mrLogMonitorMapper.delete(id);
    }

    @Override
    public MrLogMonitor selectById(Long id) {
        return mrLogMonitorMapper.selectById(id);
    }

    @Override
    public Page<MrLogMonitor> page(Page<MrLogMonitor> page) {
        Integer total = mrLogMonitorMapper.pageAll(1);
        if(total>0) {
            page.setTotalItems(total);
            page.setResult(mrLogMonitorMapper.page(page, 1));
        }
        return page;
    }

}
