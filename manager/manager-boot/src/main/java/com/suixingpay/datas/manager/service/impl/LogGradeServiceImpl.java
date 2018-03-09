/**  
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood 
 * @date: 2018-03-07 13:40:30  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.LogGrade;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.mapper.LogGradeMapper;
import com.suixingpay.datas.manager.service.LogGradeService;

/**
 * 日志级别表 服务实现类
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class LogGradeServiceImpl implements LogGradeService {

    @Autowired
    private LogGradeMapper logGradeMapper;

    @Override
    public Integer insert(LogGrade logGrade) {
        return logGradeMapper.insert(logGrade);
    }

    @Override
    public Integer update(Long id, LogGrade logGrade) {
        return logGradeMapper.update(id, logGrade);
    }

    @Override
    public Integer delete(Long id) {
        return logGradeMapper.delete(id);
    }

    @Override
    public LogGrade selectById(Long id) {
        return logGradeMapper.selectById(id);
    }

    @Override
    public Page<LogGrade> page(Page<LogGrade> page) {
        Integer total = logGradeMapper.pageAll(1);
        if(total>0) {
            page.setTotalItems(total);
            page.setResult(logGradeMapper.page(page, 1));
        }
        return page;
    }

}
