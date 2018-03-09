package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.DataField;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字段对应表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataFieldMapper {

    /**
     * 新增
     * @param dataField
     */
    Integer insert(DataField dataField);

    /**
     * 修改
     * @param dataField
     */
    Integer update(@Param("id") Long id, @Param("dataField") DataField dataField);

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
    DataField selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<DataField> page(@Param("page") Page<DataField> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}