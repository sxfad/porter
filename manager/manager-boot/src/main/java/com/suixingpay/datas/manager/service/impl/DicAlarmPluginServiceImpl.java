/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-08 10:46:01  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.DicAlarmPluginMapper;
import com.suixingpay.datas.manager.service.DicAlarmPluginService;

/**
 * 告警配置策略字典表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Service
public class DicAlarmPluginServiceImpl implements DicAlarmPluginService {

    @Autowired
    private DicAlarmPluginMapper dicAlarmPluginMapper;

}
