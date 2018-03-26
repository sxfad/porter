package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.service.JobTasksService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 同步任务表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "同步任务表管理")
@RestController
@RequestMapping("/jobtasks")
public class JobTasksController {

    @Autowired
    protected JobTasksService jobTasksService;

    /**
     * 查询明细
     *
     * @author FuZizheng
     * @date 2018/3/26 下午1:51
     * @param: [id]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTasks jobTasks = jobTasksService.selectById(id);
        return ok(jobTasks);
    }

    /**
     * 查询分页
     *
     * @author FuZizheng
     * @date 2018/3/26 上午11:41
     * @param: [pageNo, pageSize, jobName, beginTime, endTime]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                @RequestParam(value = "jobName", required = false) String jobName,
                                @RequestParam(value = "beginTime", required = false) String beginTime,
                                @RequestParam(value = "endTime", required = false) String endTime) {
        Page<JobTasks> page = jobTasksService.page(new Page<>(pageNo, pageSize), jobName, beginTime, endTime);
        return ok(page);
    }

/*    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.insert(jobTasks);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.update(id, jobTasks);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        jobTasksService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTasks jobTasks = jobTasksService.selectById(id);
        return ok(jobTasks);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<JobTasks> page = jobTasksService.page(new Page<JobTasks>(pageNo, pageSize));
        return ok(page);
    }*/

}