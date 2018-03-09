package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.web.page.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登陆用户表 Mapper接口
 * 
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CUserMapper {

    /**
     * 新增
     * @param cuser
     */
    Integer insert(CUser cuser);

    /**
     * 修改
     * @param cuser
     */
    Integer update(@Param("id") Long id, @Param("cuser") CUser cuser);

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
    CUser selectById(Long id);

    /**
     * 分頁
     * @return
     */
    List<CUser> page(@Param("page") Page<CUser> page, @Param("state") Integer state);

    /**
     * 分頁All
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

}