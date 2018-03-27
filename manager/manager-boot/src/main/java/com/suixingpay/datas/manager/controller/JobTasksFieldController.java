/*package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.JobTasksField;
import com.suixingpay.datas.manager.service.JobTasksFieldService;
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

*//**
 * 任务数据字段对照关系表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-26 14:27:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-26 14:27:55
 *//*
@Api(description = "任务数据字段对照关系表管理")
@RestController
@RequestMapping("/jobtasksfield")
public class JobTasksFieldController {

    @Autowired
    protected JobTasksFieldService jobTasksFieldService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody JobTasksField jobTasksField) {
        Integer number = jobTasksFieldService.insert(jobTasksField);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改任务数据字段对照关系表", notes = "修改任务数据字段对照关系表")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody JobTasksField jobTasksField) {
        Integer number = jobTasksFieldService.update(id, jobTasksField);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        jobTasksFieldService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTasksField jobTasksField = jobTasksFieldService.selectById(id);
        return ok(jobTasksField);
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(
            @RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<JobTasksField> page = jobTasksFieldService.page(new Page<JobTasksField>(pageNo, pageSize));
        return ok(page);
    }
}
*/