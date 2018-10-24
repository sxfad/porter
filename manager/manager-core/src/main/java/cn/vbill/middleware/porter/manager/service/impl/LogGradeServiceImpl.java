/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.LogGrade;
import cn.vbill.middleware.porter.manager.core.enums.LogLevelEnum;
import cn.vbill.middleware.porter.manager.core.mapper.LogGradeMapper;
import cn.vbill.middleware.porter.manager.service.LogGradeService;
import cn.vbill.middleware.porter.manager.web.page.Page;
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
        if (null != logGrade.getId()) {
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

        if (null != logGrade) {
            return logGrade;
        } else {
            //默认为INFO
            LogGrade logGradeDefault = new LogGrade();
            logGradeDefault.setLogLevel(LogLevelEnum.INFO);
            return logGradeDefault;
        }
    }

}
