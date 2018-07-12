package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.CRole;

import java.util.List;

/**
 * 角色表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CRoleService {

    List<CRole> findAll();

    List<CRole> findList();
}
