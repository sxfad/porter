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

import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksMonitor;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务泳道实时监控表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksMonitorMapper {

    /**
     * 新增
     *
     * @param mrJobTasksMonitor
     */
    Integer insert(MrJobTasksMonitor mrJobTasksMonitor);

    /**
     * 修改
     *
     * @param mrJobTasksMonitor
     */
    Integer update(@Param("id") Long id, @Param("mrJobTasksMonitor") MrJobTasksMonitor mrJobTasksMonitor);

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
    MrJobTasksMonitor selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<MrJobTasksMonitor> page(@Param("page") Page<MrJobTasksMonitor> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    List<MrJobTasksMonitor> selectByJobSwimlane(@Param("jobId") String jobId, @Param("swimlaneId") String swimlaneId,
                                                @Param("schemaTable") String schemaTable, @Param("startRow") Long startRow,
                                                @Param("intervalSize") Long intervalSize);

}