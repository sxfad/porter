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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.LogGrade;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志级别表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface LogGradeMapper {

    /**
     * 新增
     *
     * @param logGrade
     */
    Integer insertSelective(LogGrade logGrade);

    /**
     * 修改
     *
     * @param logGrade
     */
    Integer updateSelective(@Param("id") Long id, @Param("logGrade") LogGrade logGrade);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    LogGrade selectById(Long id);

    /**
     * 根據主鍵id查找數據
     *
     * @param maxId
     * @return
     */
    LogGrade selectByMaxId(Long maxId);

    /**
     * 分頁
     *
     * @return
     */
    List<LogGrade> page(@Param("page") Page<LogGrade> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 查询最大id数据
     *
     * @return
     */
    LogGrade select();
}