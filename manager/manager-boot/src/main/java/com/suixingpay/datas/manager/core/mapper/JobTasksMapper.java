package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.web.page.Page;
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
     * 修改
     *
     * @param jobTasks
     */
    Integer update(@Param("id") Long id, @Param("jobTasks") JobTasks jobTasks);

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
    List<JobTasks> page(@Param("page") Page<JobTasks> page,
                        @Param("state") Integer state,
                        @Param("jobName") String jobName,
                        @Param("beginTime") String beginTime,
                        @Param("endTime") String endTime);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state,
                    @Param("jobName") String jobName,
                    @Param("beginTime") String beginTime,
                    @Param("endTime") String endTime);

    /**
     * 修改任务状态
     *
     * @param id
     * @param code
     * @return
     */
    Integer updateState(@Param("id") Long id, @Param("code") String code);
}