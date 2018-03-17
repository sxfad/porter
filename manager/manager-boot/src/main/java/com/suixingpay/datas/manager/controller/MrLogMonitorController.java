package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.MrLogMonitor;
import com.suixingpay.datas.manager.service.MrLogMonitorService;
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
 * 日志监控信息表 controller控制器
 * 
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "日志监控信息表管理")
@RestController
@RequestMapping("/mrlogmonitor")
public class MrLogMonitorController {

    @Autowired
    protected MrLogMonitorService mrLogMonitorService;

    /**
     * 查询列表
     *
     * @author FuZizheng
     * @date 2018/3/16 下午5:09
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                @RequestParam(value = "state", required = false) Integer state,
                                @RequestParam(value = "beginTime", required = false) String beginTime,
                                @RequestParam(value = "endTime", required = false) String endTime) {
        Page<MrLogMonitor> page = mrLogMonitorService.page(new Page<MrLogMonitor>(pageNo, pageSize), ipAddress, state, beginTime, endTime);
        return ok(page);
    }
    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody MrLogMonitor mrLogMonitor) {
        Integer number = mrLogMonitorService.insert(mrLogMonitor);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody MrLogMonitor mrLogMonitor) {
        Integer number = mrLogMonitorService.update(id, mrLogMonitor);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        mrLogMonitorService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        MrLogMonitor mrLogMonitor = mrLogMonitorService.selectById(id);
        return ok(mrLogMonitor);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<MrLogMonitor> page = mrLogMonitorService.page(new Page<MrLogMonitor>(pageNo, pageSize));
        return ok(page);
    }*/

}