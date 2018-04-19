package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.CMenu;

import java.util.List;

/**
 * 菜单目录表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CMenuService {

    CMenu menuTree(String roleCode);

    CMenu findAll();

    Integer insert(CMenu cMenu);

    List<CMenu> findByFatherCode(String fatherCode);

    Integer update(Long id, CMenu cMenu);

    CMenu findById(Long id);

    Integer delete(Long id);
}
