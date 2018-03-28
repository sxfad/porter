package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 告警配置策略内容表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmPluginMapper {

    /**
     * 新增
     *
     * @param alarmPlugin
     */
    Integer insert(AlarmPlugin alarmPlugin);

    /**
     * 修改
     *
     * @param alarmPlugin
     */
    Integer update(@Param("id") Long id, @Param("alarmPlugin") AlarmPlugin alarmPlugin);

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
    AlarmPlugin selectById(Long id);

    /**
     * 根据告警id查找数据
     *
     * @param alarmId
     * @return
     */
    List<AlarmPlugin> selectByAlarmId(Long alarmId);

    /**
     * 分頁
     *
     * @return
     */
    List<AlarmPlugin> page(@Param("page") Page<AlarmPlugin> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 校验新增
     *
     * @param alarmPlugin
     */
    Integer insertSelective(AlarmPlugin alarmPlugin);

    /**
     * 检验修改
     *
     * @param id
     * @param alarmPlugin
     */
    Integer updateSelective(@Param("id") Long id, @Param("alarmPlugin") AlarmPlugin alarmPlugin);

}