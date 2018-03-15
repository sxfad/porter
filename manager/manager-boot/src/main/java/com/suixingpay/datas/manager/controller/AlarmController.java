package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.core.entity.Alarm;
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
@RequestMapping("/alarm")
public class AlarmController {

    @Autowired
    protected AlarmService alarmService;

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Alarm alarm) {
        Integer number = alarmService.insert(alarm);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody Alarm alarm) {
        Integer number = alarmService.update(id, alarm);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = alarmService.delete(id);
        return ok(number);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        Alarm alarm = alarmService.selectById(id);
        return ok(alarm);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<Alarm> page = alarmService.page(new Page<Alarm>(pageNo, pageSize));
        return ok(page);
    }

    @PostMapping("/insertSelective")
    @ApiOperation(value = "验证新增", notes = "验证新增")
    public ResponseMessage insertSelective(@RequestBody Alarm alarm) {
        Integer number = alarmService.insertSelective(alarm);
        return ok(number);
    }

    @PutMapping("/updateSelective/{id}")
    @ApiOperation(value = "验证修改", notes = "验证修改")
    public ResponseMessage updateSelective(@PathVariable("id") long id, @RequestBody Alarm alarm) {
        Integer number = alarmService.updateSelective(id, alarm);
        return ok(number);
    }*/

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Alarm alarm) {
        Integer number = alarmService.insert(alarm);
        return ok(number);
    }

    @GetMapping("/info")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        Alarm alarm = alarmService.selectById(id);
        return ok(alarm);
    }
}