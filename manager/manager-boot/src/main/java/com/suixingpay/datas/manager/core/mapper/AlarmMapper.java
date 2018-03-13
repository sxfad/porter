package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警配置表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmMapper {

    /**
     * 新增
     * @param alarm
     */
    Integer insert(Alarm alarm);

    /**
     * 修改
     * @param alarm
     */
    Integer update(@Param("id") Long id, @Param("alarm") Alarm alarm);

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
    Alarm selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<Alarm> page(@Param("page") Page<Alarm> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 验证新增
     * @param alarm
     * @return
     */
    Integer insertSelective(Alarm alarm);

    /**
     * 验证修改
     * @param id
     * @param alarm
     * @return
     */
    Integer updateSelective(@Param("id") Long id, @Param("alarm") Alarm alarm);

}