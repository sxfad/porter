package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.LogGrade;
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 日志级别表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface LogGradeService {

    Integer insert(LogGrade logGrade);

    Integer update(Long id, LogGrade logGrade);

    Integer delete(Long id);

    LogGrade selectById(Long id);

    Page<LogGrade> page(Page<LogGrade> page);

    LogGrade select();
}
