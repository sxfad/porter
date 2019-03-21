/**
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 任务所有权控制表 controller控制器
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Api(description="任务所有权控制表管理")
@RestController
@RequestMapping("/jobtasksowner")
public class JobTasksOwnerController {

    @Autowired
    protected JobTasksOwnerService jobTasksOwnerService;

    /**
     * 新增任务owner
     *
     * @param jobTasksOwner
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody JobTasksOwner jobTasksOwner) {
        Integer number = jobTasksOwnerService.insert(jobTasksOwner);
        return ok(number);
    }

    /**
     * 修改任务owner
     *
     * @param id
     * @param jobTasksOwner
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody JobTasksOwner jobTasksOwner) {
        Integer number = jobTasksOwnerService.update(id, jobTasksOwner);
        return ok(number);
    }

    /**
     * 删除任务owner
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        jobTasksOwnerService.delete(id);
        return ok();
    }

    /**
     * 查询明细
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTasksOwner jobTasksOwner = jobTasksOwnerService.selectById(id);
        return ok(jobTasksOwner);
    }

    /**
     * 查询列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<JobTasksOwner> page = jobTasksOwnerService.page(new Page<JobTasksOwner>(pageNum, pageSize));
        return ok(page);
    }
}
