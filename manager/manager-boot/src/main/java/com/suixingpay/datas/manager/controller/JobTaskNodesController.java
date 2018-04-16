/**
 *
 */
package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.JobTaskNodes;
import com.suixingpay.datas.manager.service.JobTaskNodesService;
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
 * 任务节点分发表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
@Api(description = "任务节点分发表管理")
@RestController
@RequestMapping("/manager/jobtasknodes")
public class JobTaskNodesController {

    @Autowired
    protected JobTaskNodesService jobTaskNodesService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody JobTaskNodes jobTaskNodes) {
        Integer number = jobTaskNodesService.insert(jobTaskNodes);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody JobTaskNodes jobTaskNodes) {
        Integer number = jobTaskNodesService.update(id, jobTaskNodes);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        jobTaskNodesService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTaskNodes t = jobTaskNodesService.selectById(id);
        return ok(t);
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = true) Integer pageNum,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<JobTaskNodes> page = jobTaskNodesService.page(new Page<JobTaskNodes>(pageNum, pageSize));
        return ok(page);
    }

}
