package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.DataSourcePlugin;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源信息关联表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DataSourcePluginMapper {

    /**
     * 新增
     *
     * @param dataSourcePlugin
     */
    Integer insert(DataSourcePlugin dataSourcePlugin);

    /**
     * 修改
     *
     * @param dataSourcePlugin
     */
    Integer update(@Param("id") Long id, @Param("dataSourcePlugin") DataSourcePlugin dataSourcePlugin);

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
    DataSourcePlugin selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DataSourcePlugin> page(@Param("page") Page<DataSourcePlugin> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}