package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.Alarm;
import com.suixingpay.datas.manager.service.AlarmService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

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
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    protected AlarmService alarmService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Alarm alarm) {
        Integer number = alarmService.insert(alarm);
        return ok(number);
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info() {
        Alarm alarm = alarmService.selectFinallyOne();
        return ok(alarm);
    }
}