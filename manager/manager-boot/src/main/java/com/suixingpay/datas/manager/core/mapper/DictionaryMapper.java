package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.Dictionary;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 数据字典表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface DictionaryMapper {

    /**
     * 新增
     *
     * @param dictionary
     */
    Integer insert(Dictionary dictionary);

    /**
     * 修改
     *
     * @param dictionary
     */
    Integer update(@Param("id") Long id, @Param("dictionary") Dictionary dictionary);

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
    Dictionary selectById(Long id);

    /**
     * 分頁
     *
     * @return
     */
    List<Dictionary> page(@Param("page") Page<Dictionary> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 所有启用数据
     *
     * @return
     */
    List<Dictionary> selectAll(@Param("state") Integer state);

}