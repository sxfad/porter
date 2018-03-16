/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import com.suixingpay.datas.manager.core.entity.LogGrade;
import com.suixingpay.datas.manager.core.enums.LogLevelEnum;
import com.suixingpay.datas.manager.core.mapper.LogGradeMapper;
import com.suixingpay.datas.manager.service.LogGradeService;
import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Integer number;
        if (null != logGrade.getId() && !"".equals(logGrade.getId())) {
            number = logGradeMapper.updateSelective(logGrade.getId(), logGrade);
        } else {
            number = logGradeMapper.insertSelective(logGrade);
        }
        return number;
    }

    @Override
    public Integer update(Long id, LogGrade logGrade) {
        return logGradeMapper.updateSelective(id, logGrade);
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
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(logGradeMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public LogGrade select() {

        LogGrade logGrade = logGradeMapper.select();

        if ( null != logGrade && !"".equals(logGrade)) {
            return logGrade;
        } else {
            //默认为INFO
            LogGrade logGradeDefault = new LogGrade();
            logGradeDefault.setLogLevel(LogLevelEnum.INFO);
            return logGradeDefault;
        }
    }

}
