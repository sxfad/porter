/**
 *
 */
package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.JobTaskNodes;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务节点分发表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
public interface JobTaskNodesMapper {

    /**
     * 新增
     *
     * @param jobTaskNodes
     */
    public Integer insert(JobTaskNodes jobTaskNodes);

    /**
     * 修改
     *
     * @param jobTaskNodes
     */
    public Integer update(@Param("id") Long id, @Param("jobTaskNodes") JobTaskNodes jobTaskNodes);

    /**
     * 刪除
     *
     * @param id
     * @return
     */
    public Integer delete(Long jobTaskId);

    /**
     * 根據jobtaskid查找數據
     *
     * @param id
     * @return
     */
    public List<JobTaskNodes> selectById(Long jobTaskId);

    /**
     * 分頁
     *
     * @return
     */
    public List<JobTaskNodes> page(@Param("page") Page<JobTaskNodes> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    public Integer pageAll(@Param("state") Integer state);

    /**
     * 保存
     * @param jobTaskId
     * @param userIds
     */
    public void insertList(@Param("jobTaskId") Long jobTaskId, @Param("nodeIds") List<String> nodeIds);
}