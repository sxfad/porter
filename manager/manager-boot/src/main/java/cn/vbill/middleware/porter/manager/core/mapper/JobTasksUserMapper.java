package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.JobTasksUser;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * job_tasks_user Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface JobTasksUserMapper {

    /**
     * 新增
     *
     * @param jobTasksUser
     */
    Integer insert(JobTasksUser jobTasksUser);

    /**
     * 修改
     *
     * @param jobTasksUser
     */
    Integer update(@Param("id") Long id, @Param("jobTasksUser") JobTasksUser jobTasksUser);

    /**
     * 刪除
     *
     * @param jobTaskId
     * @return
     */
    Integer delete(Long jobTaskId);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    JobTasksUser selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasksUser> page(@Param("page") Page<JobTasksUser> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增告警人信息
     * 
     * @param jobTaskId
     * @param userIds
     */
    void insertList(@Param("jobTaskId") Long jobTaskId, @Param("userIds") List<Long> userIds);
}