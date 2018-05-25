package com.suixingpay.datas.manager.core.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.web.page.Page;

 /**  
 * ogg表数据信息 Mapper接口
 * @author: FairyHood
 * @date: 2018-05-25 16:30:41
 * @version: V1.0-auto
 * @review: FairyHood/2018-05-25 16:30:41
 */
public interface OggTablesMapper {

    /**
     * 新增(插入非空字段)
     * @param oggTables
     * @return Integer
     */
    public Integer insert(OggTables oggTables);

    /**
     * 新增(插入全部字段)
     * @param oggTables
     * @return Integer
     */
    public Integer insertAllColumn(OggTables oggTables);

    /**
     * 修改(修改非空字段)
     * @param oggTables
     * @return Integer
     */
    public Integer update(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 修改(修改全部字段)
     * @param oggTables
     * @return Integer
     */
    public Integer updateAllColumn(@Param("id") Long id, @Param("oggTables") OggTables oggTables);

    /**
     * 根据主键查找实体
     * @param id
     * @return OggTables
     */
    public OggTables selectById(Long id);

    /**
     * list数据
     * @return List
     */
    public List<OggTables> selectList();

    /**
     * 分頁total
     * @param other
     * @return Integer
     */
    public Integer pageAll(@Param("other") String other);

    /**
     * 分頁
     * @param page
     * @param other
     * @return List
     */
    public List<OggTables> page(@Param("page") Page<OggTables> page, @Param("other") String other);

    /**
     * 刪除
     * @param id
     * @return Integer
     */
    public Integer delete(Long id);
}