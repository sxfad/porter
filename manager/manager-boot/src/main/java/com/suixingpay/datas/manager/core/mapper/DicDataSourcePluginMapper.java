package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.DicDataSourcePlugin;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据源信息字典表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
public interface DicDataSourcePluginMapper {

    /**
     * 新增
     *
     * @param dicDataSourcePlugin
     */
    Integer insert(DicDataSourcePlugin dicDataSourcePlugin);

    /**
     * 修改
     *
     * @param dicDataSourcePlugin
     */
    Integer update(@Param("id") Long id, @Param("dicDataSourcePlugin") DicDataSourcePlugin dicDataSourcePlugin);

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
    DicDataSourcePlugin selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DicDataSourcePlugin> page(@Param("page") Page<DicDataSourcePlugin> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 根据数据源类型查询数据源页面字段
     *
     * @param sourceType
     * @return
     */
    List<DicDataSourcePlugin> findByType(@Param("sourceType") String sourceType);


}