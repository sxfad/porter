package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.icon.MrNodeMonitor;
import cn.vbill.middleware.porter.manager.service.MrNodesMonitorService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 节点任务实时监控表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "节点任务实时监控表管理")
@RestController
@RequestMapping("/manager/mrnodesmonitor")
public class MrNodesMonitorController {

    @Autowired
    protected MrNodesMonitorService mrNodesMonitorService;

    /**
     * 节点实时数据(按分)
     *
     * @author FuZizheng
     * @date 2018/4/13 下午2:13
     * @param: [nodeId, intervalTime, intervalCount]
     * @return: ResponseMessage
     */
    @GetMapping("/nodeMonitor")
    @ApiOperation(value = "节点实时数据(按分)", notes = "节点实时数据(按分)")
    public ResponseMessage nodeMonitor(@RequestParam(value = "nodeId", required = false) String nodeId,
                                       @RequestParam(value = "intervalTime", required = false) Long intervalTime,
                                       @RequestParam(value = "intervalCount", required = false) Long intervalCount) {
        MrNodeMonitor mrNodeMonitor = mrNodesMonitorService.obNodeMonitor(nodeId, intervalTime, intervalCount);
        return ok(mrNodeMonitor);
    }

//    @PostMapping
//    @ApiOperation(value = "新增", notes = "新增")
//    public ResponseMessage add(@RequestBody MrNodesMonitor mrNodesMonitor) {
//        Integer number = mrNodesMonitorService.insert(mrNodesMonitor);
//        return ok(number);
//    }
//
//    @PutMapping("/{id}")
//    @ApiOperation(value = "修改", notes = "修改")
//    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody MrNodesMonitor mrNodesMonitor) {
//        Integer number = mrNodesMonitorService.update(id, mrNodesMonitor);
//        return ok(number);
//    }
//
//    @DeleteMapping("/{id}")
//    @ApiOperation(value = "删除", notes = "删除")
//    public ResponseMessage delete(@PathVariable("id") Long id) {
//        mrNodesMonitorService.delete(id);
//        return ok();
//    }
//
//    @GetMapping("/{id}")
//    @ApiOperation(value = "查询明细", notes = "查询明细")
//    public ResponseMessage info(@PathVariable("id") Long id) {
//        MrNodesMonitor mrNodesMonitor = mrNodesMonitorService.selectById(id);
//        return ok(mrNodesMonitor);
//    }
//
//    @ApiOperation(value = "查询列表", notes = "查询列表")
//    @GetMapping
//    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        Page<MrNodesMonitor> page = mrNodesMonitorService.page(new Page<MrNodesMonitor>(pageNo, pageSize));
//        return ok(page);
//    }

}