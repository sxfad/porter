package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.LogGrade;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 日志级别表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface LogGradeMapper {

    /**
     * 新增
     * @param logGrade
     */
    Integer insert(LogGrade logGrade);

    /**
     * 修改
     * @param logGrade
     */
    Integer update(@Param("id") Long id, @Param("logGrade") LogGrade logGrade);

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
    LogGrade selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<LogGrade> page(@Param("page") Page<LogGrade> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}