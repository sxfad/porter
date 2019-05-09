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

package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.web.page.Page;
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
     *
     * @param cuser
     */
    Integer insert(CUser cuser);

    /**
     * 修改
     *
     * @param cuser
     */
    Integer updateSelective(@Param("id") Long id, @Param("cuser") CUser cuser);

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
    CUser selectById(Long id);

    /**
     * 根据登陆账户 密码查询
     *
     * @param loginName
     * @param loginpw
     * @return
     */
    CUser selectByNameAndpasswd(@Param("loginname") String loginName, @Param("loginpw") String loginpw);

    /**
     * 分頁
     *
     * @return
     */
    List<CUser> page(@Param("page") Page<CUser> page, @Param("state") Integer state);

    /**
     * 分頁All
     *
     * @return
     */
    Integer pageAll(@Param("state") Integer state);

    /**
     * 全部
     *
     * @return
     */
    List<CUser> list();

    /**
     * 根据 jobTasksId 查询告警人信息
     * 
     * @param jobTasksId
     * @return
     */
    List<CUser> selectByJobTasksId(Long jobTasksId);

    /**
     * 根据alarmid查询通知人信息
     * 
     * @param alarmId
     * @return
     */
    List<CUser> selectByAlarmId(Long alarmId);

    /**
     * 验证邮箱或登录名是否重复
     *
     * @param loginname
     * @param email
     * @return
     */
    CUser findByNameOrEmail(@Param("loginname") String loginname, @Param("email") String email);

    /**
     * 修改状态
     *
     * @param id
     * @param state
     * @return
     */
    Integer updateState(@Param("id") Long id, @Param("state") Integer state);

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
    Long checkLoginName(@Param("loginName") String loginName);

    /**
     * 根据jobId查询job_tasks_owner用户详细信息
     *
     * @param jobId
     * @param type
     * @return
     */
    List<CUser> selectOwnersByJobId(@Param("jobId") Long jobId, @Param("type") Integer type);

    /**
     * 注册用户列表
     *
     * @return
     */
    List<CUser> findRegister();

    /**
     * 根据jobId查询job_tasks_owner用户详细信息
     *
     * @param nodeId
     * @param type
     * @return
     */
    List<CUser> selectOwnersByNodeId(@Param("nodeId") String nodeId, @Param("type") Integer type);
}