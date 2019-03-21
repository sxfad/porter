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

import cn.vbill.middleware.porter.manager.core.dto.RoleDataControl;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 同步任务表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksMapper {

    /**
     * 新增
     *
     * @param jobTasks
     */
    Integer insert(JobTasks jobTasks);

    /**
     * 新增
     *
     * @param jobTasks
     */
    Integer insertCapture(JobTasks jobTasks);

    /**
     * 新增
     *
     * @param jobTasks
     */
    Integer insertZKCapture(JobTasks jobTasks);

    /**
     * 修改
     *
     * @param jobTasks
     */
    Integer update(@Param("jobTasks") JobTasks jobTasks);

    /**
     * 修改
     *
     * @param jobTasks
     */
    Integer updateZKCapture(@Param("jobTasks") JobTasks jobTasks);

    /**
     * 逻辑刪除
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
    JobTasks selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasks> page(@Param("page") Page<JobTasks> page, @Param("state") Integer state, @Param("jobType") Integer jobType,
                        @Param("jobName") String jobName, @Param("jobId") Long jobId, @Param("beginTime") String beginTime,
                        @Param("endTime") String endTime, @Param("jobState") String jobState, @Param("id") Long id,
                        @Param("roleDataControl") RoleDataControl roleDataControl);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("jobType") Integer jobType, @Param("jobName") String jobName,
                    @Param("jobId") Long jobId, @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                    @Param("jobState") String jobState, @Param("id") Long id, @Param("roleDataControl") RoleDataControl roleDataControl);

    /**
     * 修改任务状态
     *
     * @param id
     * @param code
     * @return
     */
    Integer updateState(@Param("id") Long id, @Param("code") String code);

    /**
     * selectList
     *
     * @date 2018/8/9 下午6:15
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.JobTasks>
     */
    List<JobTasks> selectList();

    /**
     * selectJobNameList
     *
     * @author FuZizheng
     * @date 2018/8/9 下午6:15
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.JobTasks>
     */
    List<JobTasks> selectJobNameList();

    /**
     * 显示任务id下拉框
     *
     * @author FuZizheng
     * @date 2019/2/19 2:17 PM
     * @param: []
     * @return: java.util.List<java.lang.Long>
     */
    List<Long> showjobIdList();
}
