/**
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务所有权控制表 Mapper接口
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface JobTasksOwnerMapper {

    /**
     * 新增
     * @param jobTasksOwner
     */
    Integer insert(JobTasksOwner jobTasksOwner);

    /**
     * 修改
     * @param jobTasksOwner
     */
    Integer update(@Param("id") Long id, @Param("jobTasksOwner") JobTasksOwner jobTasksOwner);

    /**
     * 刪除
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 根據主鍵id查找數據
     * @param id
     * @return
     */
    JobTasksOwner selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<JobTasksOwner> page(@Param("page") Page<JobTasksOwner> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}