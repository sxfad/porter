package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.JobTasksTable;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务数据表对照关系表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksTableMapper {

    /**
     * 新增
     *
     * @param jobTasksTable
     */
    Integer insert(JobTasksTable jobTasksTable);

    /**
     * 修改
     *
     * @param jobTasksTable
     */
    Integer update(@Param("id") Long id, @Param("jobTasksTable") JobTasksTable jobTasksTable);

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
     * @param jobTaskId
     * @return
     */
    List<JobTasksTable> selectById(Long jobTaskId);

    /**
     * 分頁
     *
     * @return
     */
    List<JobTasksTable> page(@Param("page") Page<JobTasksTable> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增 JobTasksTable
     *
     * @param tables
     */
    void insertList(List<JobTasksTable> tables);

}