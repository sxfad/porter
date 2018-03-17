package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.service.NodesService;
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
 * 节点信息表 controller控制器
 * 
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "节点信息表管理")
@RestController
@RequestMapping("/nodes")
public class NodesController {

    @Autowired
    protected NodesService nodesService;

    /**
     * 查询列表
     *
     * @author FuZizheng
     * @date 2018/3/16 下午3:34
     * @param: [pageNo, pageSize, ipAddress, state, machineName, type]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @RequestParam(value = "ipAddress", required = false) String ipAddress,
                                @RequestParam(value = "state", required = false) Integer state,
                                @RequestParam(value = "machineName", required = false) String machineName,
                                @RequestParam(value = "type", required = false) Integer type) {
        Page<Nodes> page = nodesService.page(new Page<Nodes>(pageNo, pageSize), ipAddress, state, machineName, type);
        return ok(page);
    }

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Nodes nodes) {
        Integer number = nodesService.insert(nodes);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody Nodes nodes) {
        Integer number = nodesService.update(id, nodes);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        nodesService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        Nodes nodes = nodesService.selectById(id);
        return ok(nodes);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<Nodes> page = nodesService.page(new Page<Nodes>(pageNo, pageSize));
        return ok(page);
    }*/

}