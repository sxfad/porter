/**
 *
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.core.mapper.CUserMapper;
import com.suixingpay.datas.manager.service.CUserService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * 登陆用户表 服务实现类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
@Service
public class CUserServiceImpl implements CUserService {

    @Autowired
    private CUserMapper cuserMapper;

    @Override
    public Integer insert(CUser cuser) {
        return cuserMapper.insert(cuser);
    }

    @Override
    public Integer update(Long id, CUser cuser) {
        return cuserMapper.updateSelective(id, cuser);
    }

    @Override
    public Integer delete(Long id) {
        return cuserMapper.delete(id);
    }

    @Override
    public CUser selectById(Long id) {
        return cuserMapper.selectById(id);
    }

    @Override
    public CUser selectByNameAndpasswd(String LoginName, String passwd) {
        return cuserMapper.selectByNameAndpasswd(LoginName, passwd);
    }

    @Override
    public Page<CUser> page(Page<CUser> page) {
        Integer total = cuserMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(cuserMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public List<CUser> list() {
        return cuserMapper.list();
    }

    @Override
    public List<CUser> selectByJobTasksId(Long JobTasksId) {
        return cuserMapper.selectByJobTasksId(JobTasksId);
    }

    @Override
    public List<CUser> selectByAlarmId(Long alarmId) {
        return cuserMapper.selectByAlarmId(alarmId);
    }

}
