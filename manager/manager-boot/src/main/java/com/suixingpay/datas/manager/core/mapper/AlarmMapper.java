package com.suixingpay.datas.manager.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.web.page.Page;

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
    public Integer insert(Alarm alarm);
    
    /**
     * 修改
     * @param alarm
     */
    public Integer update(@Param("id")Long id,@Param("alarm") Alarm alarm);
    
    /**
     * 刪除
     * @param id
     * @return
     */
    public Integer delete(Long id);
    
    /**
     * 根據主鍵id查找數據
     * @param id
     * @return
     */
    public Alarm selectById(Long id);
    
    /**
     * 分頁
     * @return
     */
    public List<Alarm> page(@Param("page")Page<Alarm> page,@Param("state")Integer state);
    
    /**
     * 分頁All
     * @return
     */
    public Integer pageAll(@Param("state")Integer state);

}