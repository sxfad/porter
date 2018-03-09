package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import com.suixingpay.datas.manager.web.page.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.core.entity.AlarmUser;
import com.suixingpay.datas.manager.service.AlarmUserService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 告警用户关联表 controller控制器
 * 
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "告警用户关联表管理")
@RestController
@RequestMapping("/alarmuser")
public class AlarmUserController {

    @Autowired
    protected AlarmUserService alarmUserService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody AlarmUser alarmUser) {
        Integer number = alarmUserService.insert(alarmUser);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody AlarmUser alarmUser) {
        Integer number = alarmUserService.update(id, alarmUser);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        alarmUserService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        AlarmUser alarmUser = alarmUserService.selectById(id);
        return ok(alarmUser);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<AlarmUser> page = alarmUserService.page(new Page<AlarmUser>(pageNo, pageSize));
        return ok(page);
    }

}