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

import cn.vbill.middleware.porter.common.alert.AlertReceiver;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.AlertConfigPushCommand;
import cn.vbill.middleware.porter.common.config.AlertConfig;
import cn.vbill.middleware.porter.manager.core.entity.Alarm;
import cn.vbill.middleware.porter.manager.core.entity.AlarmPlugin;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.service.AlarmService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 告警配置表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Api(description = "告警配置表管理")
@RestController
@RequestMapping("/manager/alarm")
public class AlarmController {

    @Autowired
    protected AlarmService alarmService;

    /**
     * 新增
     *
     * @date 2018/8/9 下午4:18
     * @param: [alarm]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Alarm alarm) throws Exception {
        Integer number = alarmService.insert(alarm);
        if (number == 1) {
            Alarm alarms = alarmService.selectById(alarm.getId());
            AlertReceiver[] receiver = receiver(alarms.getCusers());
            Map<String, String> client = fieldsMap(alarms.getAlarmPlugins());
            ClusterProviderProxy.INSTANCE
                    .broadcast(new AlertConfigPushCommand(new AlertConfig(alarms.getAlarmType(), receiver, client)));
        }
        return ResponseMessage.ok(number);
    }

    /**
     * info
     *
     * @date 2018/8/9 下午4:19
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/info")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info() {
        Alarm alarm = alarmService.selectFinallyOne();
        return ResponseMessage.ok(alarm);
    }

    /**
     * receiver
     *
     * @date 2018/8/9 下午4:19
     * @param: [cusers]
     * @return: cn.vbill.middleware.porter.common.alert.AlertReceiver[]
     */
    private AlertReceiver[] receiver(List<CUser> cusers) {
        AlertReceiver[] alertReceivers = new AlertReceiver[cusers.size()];
        for (int i = 0; i < cusers.size(); i++) {
            alertReceivers[i] = new AlertReceiver(cusers.get(i).getNickname(), cusers.get(i).getEmail(),
                    cusers.get(i).getMobile());
        }
        return alertReceivers;
    }

    /**
     * fieldsMap
     *
     * @date 2018/8/9 下午4:19
     * @param: [alarmPlugins]
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    private Map<String, String> fieldsMap(List<AlarmPlugin> alarmPlugins) {
        Map<String, String> map = new HashMap<>();
        for (AlarmPlugin alarmPlugin : alarmPlugins) {
            map.put(alarmPlugin.getPluginCode(), alarmPlugin.getPluginValue());
        }
        return map;
    }
}