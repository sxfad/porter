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

import cn.vbill.middleware.porter.manager.core.entity.DicAlarmPlugin;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;

/**
 * 告警配置策略字典表 服务接口类
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */

public interface DicAlarmPluginService {

    Integer insert(DicAlarmPlugin dicAlarmPlugin);

    Integer update(Long id, DicAlarmPlugin dicAlarmPlugin);

    Integer delete(Long id);

    DicAlarmPlugin selectById(Long id);

    Page<DicAlarmPlugin> page(Page<DicAlarmPlugin> page);

    List<DicAlarmPlugin> findByAlertType(String alertType);

}
