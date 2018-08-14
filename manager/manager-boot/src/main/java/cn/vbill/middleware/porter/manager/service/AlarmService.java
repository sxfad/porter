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
import cn.vbill.middleware.porter.manager.web.page.Page;

/**
 * 告警配置表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
public interface AlarmService {

    /**
     * selectFinallyOne
     *
     * @date 2018/8/10 上午10:12
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.core.entity.Alarm
     */
    Alarm selectFinallyOne();

    /**
     * 新增
     *
     * @date 2018/8/10 上午10:12
     * @param: [alarm]
     * @return: java.lang.Integer
     */
    Integer insert(Alarm alarm);

    /**
     * 更新
     *
     * @date 2018/8/10 上午10:12
     * @param: [id, alarm]
     * @return: java.lang.Integer
     */
    Integer update(Long id, Alarm alarm);

    /**
     * 删除
     *
     * @date 2018/8/10 上午10:12
     * @param: [id]
     * @return: java.lang.Integer
     */
    Integer delete(Long id);

    /**
     * 根据id查询
     *
     * @date 2018/8/10 上午10:13
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.core.entity.Alarm
     */
    Alarm selectById(Long id);

    /**
     * 分页
     *
     * @date 2018/8/10 上午10:13
     * @param: [page]
     * @return: cn.vbill.middleware.porter.manager.web.page.Page<cn.vbill.middleware.porter.manager.core.entity.Alarm>
     */
    Page<Alarm> page(Page<Alarm> page);

    /**
     * 条件新增
     *
     * @date 2018/8/10 上午10:13
     * @param: [alarm]
     * @return: java.lang.Integer
     */
    Integer insertSelective(Alarm alarm);

    /**
     * 条件修改
     *
     * @date 2018/8/10 上午10:13
     * @param: [id, alarm]
     * @return: java.lang.Integer
     */
    Integer updateSelective(Long id, Alarm alarm);
}
