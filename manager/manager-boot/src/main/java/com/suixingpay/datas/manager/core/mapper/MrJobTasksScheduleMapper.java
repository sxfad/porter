package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.MrJobTasksSchedule;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务泳道进度表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface MrJobTasksScheduleMapper {

    /**
     * 新增
     *
     * @param mrJobTasksSchedule
     */
    Integer insert(MrJobTasksSchedule mrJobTasksSchedule);

    /**
     * 修改
     *
     * @param mrJobTasksSchedule
     */
    Integer update(@Param("id") Long id, @Param("mrJobTasksSchedule") MrJobTasksSchedule mrJobTasksSchedule);

    /**
     * 修改 判断为空字段
     *
     * @param id
     * @param mrJobTasksSchedule
     * @return
     */
    Integer updateSelective(@Param("id") Long id, @Param("mrJobTasksSchedule") MrJobTasksSchedule mrJobTasksSchedule);

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
    MrJobTasksSchedule selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<MrJobTasksSchedule> page(@Param("page") Page<MrJobTasksSchedule> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 根据任务id和泳道查询
     *
     * @param jobId
     * @param swimlaneId
     * @return
     */
    MrJobTasksSchedule selectByJobIdAndSwimlaneId(@Param("jobId") String jobId, @Param("swimlaneId") String swimlaneId,
            @Param("schemaTable") String schemaTable);

    /**
     * 根据jobId获取任务泳道
     *
     * @param jobId
     * @return
     */
    List<MrJobTasksSchedule> selectSwimlaneByJobId(String jobId);

    /**
     * 条件查询获取列表
     *
     * @param jobId
     * @param heartBeatBeginDate
     * @param heartBeatEndDate
     * @return
     */
    List<MrJobTasksSchedule> list(@Param("jobId") String jobId, @Param("heartBeatBeginDate") String heartBeatBeginDate,
            @Param("heartBeatEndDate") String heartBeatEndDate);
}