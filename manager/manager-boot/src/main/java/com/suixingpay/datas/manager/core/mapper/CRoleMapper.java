package com.suixingpay.datas.manager.core.mapper;

import com.suixingpay.datas.manager.core.entity.CRole;

import java.util.List;

/**
 * 角色表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CRoleMapper {

    List<CRole> findAll();
}