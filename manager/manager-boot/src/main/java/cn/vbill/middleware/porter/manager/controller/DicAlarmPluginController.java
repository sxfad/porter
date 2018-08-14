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

package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.service.DicAlarmPluginService;
import cn.vbill.middleware.porter.manager.core.entity.DicAlarmPlugin;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 告警配置策略字典表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Api(description = "告警配置策略字典表管理")
@RestController
@RequestMapping("/manager/dicalarmplugin")
public class DicAlarmPluginController {

    @Autowired
    protected DicAlarmPluginService dicAlarmPluginService;

    /**
     * 查询明细
     *
     * @date 2018/8/9 下午4:21
     * @param: [alertType]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/{alerttype}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage findByAlertType(@PathVariable("alerttype") String alertType) {
        List<DicAlarmPlugin> list = dicAlarmPluginService.findByAlertType(alertType);
        return ok(list);
    }

}