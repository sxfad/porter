package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.web.page.Page;

/**  
 * 告警配置表 服务接口类
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmService{  
    
    public Integer insert(Alarm alarm);
    
    public Integer update(Long id,Alarm alarm);
    
    public Integer delete(Long id);
    
    public Alarm selectById(Long id);
    
    public Page<Alarm> page(Page<Alarm> page);
}
