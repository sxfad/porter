/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.web.page.Page;

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

    /**
     * 新增
     *
     * @date 2018/8/10 上午11:02
     * @param: [cuser]
     * @return: java.lang.Integer
     */
    Integer insert(CUser cuser);

    /**
     * 修改
     *
     * @date 2018/8/10 上午11:02
     * @param: [id, cuser]
     * @return: java.lang.Integer
     */
    Integer update(Long id, CUser cuser);

    /**
     * 删除
     *
     * @date 2018/8/10 上午11:02
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午11:02
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.event.CUser
     */
    CUser selectById(Long id);

    /**
     * selectByNameAndpasswd
     *
     * @date 2018/8/10 上午11:03
     * @param: [loginName, passwd]
     * @return: cn.vbill.middleware.porter.manager.core.event.CUser
     */
    CUser selectByNameAndpasswd(String loginName, String passwd);

    /**
     * 分页
     *
     * @date 2018/8/10 上午11:03
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.event.CUser>
     */
    Page<CUser> page(Page<CUser> page);

    /**
     * 列表
     *
     * @date 2018/8/10 上午11:03
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.event.CUser>
     */
    List<CUser> list();

    /**
     * selectByJobTasksId
     *
     * @date 2018/8/10 上午11:03
     * @param: [jobTasksId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.event.CUser>
     */
    List<CUser> selectByJobTasksId(Long jobTasksId);

    /**
     * selectByAlarmId
     *
     * @date 2018/8/10 上午11:04
     * @param: [alarmId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.event.CUser>
     */
    List<CUser> selectByAlarmId(Long alarmId);

    /**
     * findByNameOrEmail
     *
     * @date 2018/8/10 上午11:04
     * @param: [loginname, email]
     * @return: boolean
     */
    boolean findByNameOrEmail(String loginname, String email);

    /**
     * updateState
     *
     * @date 2018/8/10 上午11:04
     * @param: [id, state]
     * @return: java.lang.Integer
     */
    Integer updateState(Long id, Integer state);

    /**
     * 用户注册
     *
     * @param cUser
     * @return
     */
    Integer register(CUser cUser);

    /**
     * 检查登陆名称是否重复
     *
     * @param loginName
     * @return
     */
    Long checkLoginName(String loginName);

    /**
     * 根据jobId查询job_tasks_owner用户详细信息
     *
     * @param jobId
     * @return
     */
    List<CUser> selectOwnersByJobId(Long jobId, Integer type);

    /**
     * 注册用户列表
     *
     * @return
     */
    List<CUser> findRegister();

    /**
     * 根据NodeId查询b_nodes_owner用户详细信息
     *
     * @param nodeId
     * @return
     */
    List<CUser> selectOwnersByNodeId(Long nodeId, Integer type);
}
