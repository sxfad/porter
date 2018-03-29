package com.suixingpay.datas.manager.service;

import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.web.page.Page;

import java.util.List;

/**
 * 登陆用户表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface CUserService {

    Integer insert(CUser cuser);

    Integer update(Long id, CUser cuser);

    Integer delete(Long id);

    CUser selectById(Long id);

    CUser selectByNameAndpasswd(String loginName, String passwd);

    Page<CUser> page(Page<CUser> page);

    List<CUser> list();

    List<CUser> selectByJobTasksId(Long JobTasksId);
}
