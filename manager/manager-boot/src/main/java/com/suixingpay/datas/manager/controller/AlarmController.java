package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.AlertConfigPushCommand;
import com.suixingpay.datas.common.config.AlertConfig;
import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.service.AlarmService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
        return ok(number);
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info() {
        Alarm alarm = alarmService.selectFinallyOne();
        return ok(alarm);
    }

    private AlertReceiver[] receiver(List<CUser> cusers) {
        AlertReceiver[] alertReceivers = new AlertReceiver[cusers.size()];
        for (int i = 0; i < cusers.size(); i++) {
            alertReceivers[i] = new AlertReceiver(cusers.get(i).getNickname(), cusers.get(i).getEmail(),
                    cusers.get(i).getMobile());
        }
        return alertReceivers;
    }

    private Map<String, String> fieldsMap(List<AlarmPlugin> alarmPlugins) {
        Map<String, String> map = new HashMap<>();
        for (AlarmPlugin alarmPlugin : alarmPlugins) {
            map.put(alarmPlugin.getPluginCode(), alarmPlugin.getPluginValue());
        }
        return map;
    }
}