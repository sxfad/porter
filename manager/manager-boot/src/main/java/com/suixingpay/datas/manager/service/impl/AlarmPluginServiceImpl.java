/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-08 10:46:01  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.core.mapper.AlarmPluginMapper;
import com.suixingpay.datas.manager.service.AlarmPluginService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 告警配置策略内容表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class AlarmPluginServiceImpl implements AlarmPluginService {
	
	@Autowired
	private AlarmPluginMapper alarmPluginMapper;

	 @Override
	 public Integer insert(AlarmPlugin alarmPlugin) {
		 return alarmPluginMapper.insert(alarmPlugin);
	 }

	 @Override
	 public Integer update(Long id, AlarmPlugin alarmPlugin) {
		 return alarmPluginMapper.update(id, alarmPlugin);
	 }

	 @Override
	 public Integer delete(Long id) {
		 return alarmPluginMapper.delete(id);
	 }

	 @Override
	 public AlarmPlugin selectById(Long id) {
		 return alarmPluginMapper.selectById(id);
	 }

	 @Override
	 public Page<AlarmPlugin> page(Page<AlarmPlugin> page) {
		 Integer total = alarmPluginMapper.pageAll(1);
		 if(total>0) {
			 page.setTotalItems(total);
			 page.setResult(alarmPluginMapper.page(page, 1));
		 }
		 return page;
	 }

}
