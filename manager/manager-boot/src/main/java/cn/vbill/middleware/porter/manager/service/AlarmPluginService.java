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
import cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 告警配置策略内容表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmPluginService {

    /**
     * 新增Alarm
     *
     * @date 2018/8/10 上午10:01
     * @param: [alarm]
     * @return: void
     */
    void insert(Alarm alarm);

    /**
     * 新增AlarmPlugin
     *
     * @date 2018/8/10 上午10:01
     * @param: [alarmPlugin]
     * @return: java.lang.Integer
     */
    Integer insert(AlarmPlugin alarmPlugin);

    /**
     * 更新
     *
     * @date 2018/8/10 上午10:01
     * @param: [id, alarmPlugin]
     * @return: java.lang.Integer
     */
    Integer update(Long id, AlarmPlugin alarmPlugin);

    /**
     * 删除
     *
     * @date 2018/8/10 上午10:01
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据AlarmId删除
     *
     * @date 2018/8/10 上午10:02
     * @param: [alarmId]
     * @return: java.util.List<cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin>
     */
    List<AlarmPlugin> selectByAlarmId(Long alarmId);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午10:02
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin
     */
    AlarmPlugin selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午10:02
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin>
     */
    Page<AlarmPlugin> page(Page<AlarmPlugin> page);

    /**
     * 条件新增
     *
     * @date 2018/8/10 上午10:02
     * @param: [alarmPlugin]
     * @return: java.lang.Integer
     */
    Integer insertSelective(AlarmPlugin alarmPlugin);

    /**
     * 条件修改
     *
     * @date 2018/8/10 上午10:02
     * @param: [id, alarmPlugin]
     * @return: java.lang.Integer
     */
    Integer updateSelective(Long id, AlarmPlugin alarmPlugin);

}
