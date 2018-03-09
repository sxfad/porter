package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源信息表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataSourceMapper {

    /**
     * 新增
     * @param dataSource
     */
    Integer insert(DataSource dataSource);

    /**
     * 修改
     * @param dataSource
     */
    Integer update(@Param("id") Long id, @Param("dataSource") DataSource dataSource);

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
    DataSource selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<DataSource> page(@Param("page") Page<DataSource> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);
}