package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.DicAlarmPlugin;
import com.suixingpay.datas.manager.service.DicAlarmPluginService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

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

    @GetMapping("/{alerttype}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage findByAlertType(@PathVariable("alerttype") String alertType) {
        List<DicAlarmPlugin> list = dicAlarmPluginService.findByAlertType(alertType);
        return ok(list);
    }

}