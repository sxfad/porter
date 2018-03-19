package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.core.entity.DicAlarmPlugin;
import com.suixingpay.datas.manager.service.DicAlarmPluginService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/dicalarmplugin")
public class DicAlarmPluginController {

    @Autowired
    protected DicAlarmPluginService dicAlarmPluginService;

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DicAlarmPlugin dicAlarmPlugin) {
        Integer number = dicAlarmPluginService.insert(dicAlarmPlugin);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DicAlarmPlugin dicAlarmPlugin) {
        Integer number = dicAlarmPluginService.update(id, dicAlarmPlugin);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dicAlarmPluginService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DicAlarmPlugin dicAlarmPlugin = dicAlarmPluginService.selectById(id);
        return ok(dicAlarmPlugin);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<DicAlarmPlugin> page = dicAlarmPluginService.page(new Page<DicAlarmPlugin>(pageNo, pageSize));
        return ok(page);
    }*/

    @GetMapping("/{alerttype}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage findByAlertType(@PathVariable("alerttype") String alertType) {
        List<DicAlarmPlugin> list = dicAlarmPluginService.findByAlertType(alertType);
        return ok(list);
    }

}