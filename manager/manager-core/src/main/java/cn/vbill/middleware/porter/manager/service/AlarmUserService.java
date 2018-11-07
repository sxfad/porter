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

import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmUser;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 告警用户关联表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public interface AlarmUserService {

    /**
     * 新增
     *
     * @date 2018/8/10 上午10:15
     * @param: [alarm]
     * @return: void
     */
    void insert(Alarm alarm);

    /**
     * 新增
     *
     * @date 2018/8/10 上午10:15
     * @param: [alarmUser]
     * @return: java.lang.Integer
     */
    Integer insert(AlarmUser alarmUser);

    /**
     * 修改
     *
     * @date 2018/8/10 上午10:16
     * @param: [id, alarmUser]
     * @return: java.lang.Integer
     */
    Integer update(Long id, AlarmUser alarmUser);

    /**
     * 删除
     *
     * @date 2018/8/10 上午10:16
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午10:16
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.AlarmUser
     */
    AlarmUser selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午10:16
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.AlarmUser>
     */
    Page<AlarmUser> page(Page<AlarmUser> page);

    /**
     * 根据Alarmid查询
     *
     * @date 2018/8/10 上午10:16
     * @param: [alarmId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.AlarmUser>
     */
    List<AlarmUser> selectByAlarmId(Long alarmId);
}
