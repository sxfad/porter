package com.suixingpay.datas.manager.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * ogg表数据信息 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
public interface OggTablesMapper {

    /**
     * 新增(插入非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insert(OggTables oggTables);

    /**
     * 新增(插入全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer insertAllColumn(OggTables oggTables);

    /**
     * 修改(修改非空字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer update(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 修改(修改全部字段)
     * 
     * @param oggTables
     * @return Integer
     */
    Integer updateAllColumn(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 根据主键查找实体
     * 
     * @param id
     * @return OggTables
     */
    OggTables selectById(Long id);

    /**
     * list数据
     * 
     * @return List
     */
    List<OggTables> selectList(@Param("ipAddress") String ipAddress, @Param("tableValue") String tableValue);

    /**
     * 分頁total
     * 
     * @param other
     * @return Integer
     */
    Integer pageAll(@Param("ipAddress") String ipAddress, @Param("tableValue") String tableValue);

    /**
     * 分頁
     * 
     * @param page
     * @param other
     * @return List
     */
    List<OggTables> page(@Param("page") Page<OggTables> page, @Param("ipAddress") String ipAddress,
            @Param("tableValue") String tableValue);

    /**
     * 刪除
     * 
     * @param id
     * @return Integer
     */
    Integer delete(Long id);


    String relatedTask(@Param("tableName") String tableName);
}