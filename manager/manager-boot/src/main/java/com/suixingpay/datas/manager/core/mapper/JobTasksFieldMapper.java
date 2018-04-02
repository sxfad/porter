package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务数据字段对照关系表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 */
public interface JobTasksFieldMapper {

    /**
     * 新增
     *
     * @param jobTasksField
     */
    public Integer insert(JobTasksField jobTasksField);

    /**
     * 修改
     *
     * @param jobTasksField
     */
    public Integer update(@Param("id") Long id, @Param("jobTasksField") JobTasksField jobTasksField);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    public Integer delete(Long jobTaskId);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    public JobTasksField selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    public List<JobTasksField> page(@Param("page") Page<JobTasksField> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    public Integer pageAll(@Param("state") Integer state);

    /**
     * 批量新增
     *
     * @param fields
     */
    void insertList(List<JobTasksField> fields);

    /**
     * 根据jobTasksId和jobTasksTableId查询详情
     *
     * @param jobTaskId
     * @param jobTasksTableId
     * @return
     */
    List<JobTasksField> selectInfo(@Param("jobTaskId") Long jobTaskId, @Param("jobTasksTableId") Long jobTasksTableId);
}