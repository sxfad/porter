package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.DataTable;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据表信息表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DataTableMapper {

    /**
     * 验证新增
     *
     * @param dataTable
     * @return
     */
    Integer insertSelective(DataTable dataTable);

    /**
     * 根據主鍵id查找數據
     *
     * @param id
     * @return
     */
    DataTable selectById(Long id);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<DataTable> page(@Param("page") Page<DataTable> page, @Param("state") Integer state,
            @Param("bankName") String bankName, @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state, @Param("bankName") String bankName,
            @Param("beginTime") String beginTime, @Param("endTime") String endTime);

    /**
     * 目标/元数据表组分页All
     *
     * @param state
     * @return
     */
    Integer dataTableAll(@Param("state") Integer state);

    /**
     * 目标/元数据表组分页方法
     *
     * @param page
     * @param state
     * @return
     */
    List<DataTable> dataTablePage(@Param("page") Page<DataTable> page, @Param("state") Integer state);

    // *
    // * 新增
    // *
    // * @param dataTable
    //
    // Integer insert(DataTable dataTable);
    //
    // *
    // * 修改
    // *
    // * @param dataTable
    //
    // Integer update(@Param("id") Long id, @Param("dataTable") DataTable
    // dataTable);
    //

}