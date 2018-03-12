package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.AlarmPlugin;
import com.suixingpay.datas.manager.service.AlarmPluginService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 告警配置策略内容表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Api(description = "告警配置策略内容表管理")
@RestController
@RequestMapping("/alarmplugin")
public class AlarmPluginController {

    @Autowired
    protected AlarmPluginService alarmPluginService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody AlarmPlugin alarmPlugin) {
        Integer number = alarmPluginService.insert(alarmPlugin);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody AlarmPlugin alarmPlugin) {
        Integer number = alarmPluginService.update(id, alarmPlugin);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        alarmPluginService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        AlarmPlugin alarmPlugin = alarmPluginService.selectById(id);
        return ok(alarmPlugin);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<AlarmPlugin> page = alarmPluginService.page(new Page<AlarmPlugin>(pageNo, pageSize));
        return ok(page);
    }

    @PostMapping("/insertSelective")
    @ApiOperation(value = "验证新增", notes = "验证新增")
    public ResponseMessage insertSelective(@RequestBody AlarmPlugin alarmPlugin) {
        Integer number = alarmPluginService.insertSelective(alarmPlugin);
        return ok(number);
    }

    @PutMapping("/updateSelective/{id}")
    @ApiOperation(value = "验证修改", notes = "验证修改")
    public ResponseMessage updateSelective(@PathVariable("id") long id, @RequestBody AlarmPlugin alarmPlugin) {
        Integer number = alarmPluginService.updateSelective(id, alarmPlugin);
        return ok(number);
    }

}








