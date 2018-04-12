package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.MrNodesSchedule;
import com.suixingpay.datas.manager.service.MrNodesScheduleService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 节点任务监控表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "节点任务监控表管理")
@RestController
@RequestMapping("/manager/mrnodesschedule")
public class MrNodesScheduleController {

    @Autowired
    protected MrNodesScheduleService mrNodesScheduleService;

    /**
     * 查询分页
     *
     * @author FuZizheng
     * @date 2018/3/20 上午9:35
     * @param: [pageNo]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "computerName", required = false) String computerName) {
        Page<MrNodesSchedule> page = mrNodesScheduleService.page(new Page<MrNodesSchedule>(pageNo, pageSize), ipAddress,
                computerName);
        return ok(page);
    }

    /*
     * @PostMapping
     * 
     * @ApiOperation(value = "新增", notes = "新增") public ResponseMessage
     * add(@RequestBody MrNodesSchedule mrNodesSchedule) { Integer number =
     * mrNodesScheduleService.insert(mrNodesSchedule); return ok(number); }
     * 
     * @PutMapping("/{id}")
     * 
     * @ApiOperation(value = "修改", notes = "修改") public ResponseMessage
     * update(@PathVariable("id") Long id, @RequestBody MrNodesSchedule
     * mrNodesSchedule) { Integer number = mrNodesScheduleService.update(id,
     * mrNodesSchedule); return ok(number); }
     * 
     * @DeleteMapping("/{id}")
     * 
     * @ApiOperation(value = "删除", notes = "删除") public ResponseMessage
     * delete(@PathVariable("id") Long id) { mrNodesScheduleService.delete(id);
     * return ok(); }
     * 
     * @GetMapping("/{id}")
     * 
     * @ApiOperation(value = "查询明细", notes = "查询明细") public ResponseMessage
     * info(@PathVariable("id") Long id) { MrNodesSchedule mrNodesSchedule =
     * mrNodesScheduleService.selectById(id); return ok(mrNodesSchedule); }
     * 
     * @ApiOperation(value = "查询列表", notes = "查询列表")
     * 
     * @GetMapping public ResponseMessage list(@RequestParam(value = "pageNo",
     * required = false) Integer pageNo,
     * 
     * @RequestParam(value = "pageSize", required = false) Integer pageSize) {
     * Page<MrNodesSchedule> page = mrNodesScheduleService.page(new
     * Page<MrNodesSchedule>(pageNo, pageSize)); return ok(page); }
     */

}