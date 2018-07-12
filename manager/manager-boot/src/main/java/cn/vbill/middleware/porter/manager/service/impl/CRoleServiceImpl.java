/**
 *
 */
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.mapper.CRoleMapper;
import cn.vbill.middleware.porter.manager.service.CRoleService;
import cn.vbill.middleware.porter.manager.core.entity.CRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CRoleServiceImpl implements CRoleService {

    @Autowired
    private CRoleMapper croleMapper;

    @Override
    public List<CRole> findAll() {
        return croleMapper.findAll();
    }

    @Override
    public List<CRole> findList() {
        return croleMapper.findList();
    }
}
