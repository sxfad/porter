package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.service.NodesService;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.command.NodeOrderPushCommand;
import cn.vbill.middleware.porter.common.config.NodeCommandConfig;
import cn.vbill.middleware.porter.common.dic.NodeStatusType;
import cn.vbill.middleware.porter.common.node.NodeCommandType;
import cn.vbill.middleware.porter.manager.core.entity.Nodes;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

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
@RequestMapping("/manager/nodes")
public class NodesController {

    @Autowired
    protected NodesService nodesService;

    /**
     * 查询列表
     *
     * @author FuZizheng
     * @date 2018/3/16 下午3:34
     * @param: [pageNo,
     *             pageSize, ipAddress, state, machineName, type]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "state", required = false) Integer state,
            @RequestParam(value = "machineName", required = false) String machineName) {
        Page<Nodes> page = nodesService.page(new Page<Nodes>(pageNo, pageSize), ipAddress, state, machineName);
        return ok(page);
    }

    /**
     * 验证nodeId是否重复
     *
     * @author FuZizheng
     * @date 2018/3/28 下午2:29
     * @param: [nodeId]
     * @return: ResponseMessage
     */
    @GetMapping("/testNodeId/{nodeId}")
    @ApiOperation(value = "验证nodeId是否重复", notes = "节点Id")
    public ResponseMessage testNodeId(@PathVariable("nodeId") String nodeId) {
        boolean flag = nodesService.testNodeId(nodeId);
        return ok(flag);
    }

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody Nodes nodes) {
        Integer number = nodesService.insert(nodes);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = nodesService.delete(id);
        return ok(number);
    }

    @PostMapping("/taskpushstate")
    @ApiOperation(value = "任务状态推送", notes = "任务状态推送")
    public ResponseMessage taskPushState(@RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "taskPushState", required = true) NodeStatusType taskPushState) throws Exception {
        Integer i = nodesService.taskPushState(id, taskPushState);
        if (i == 1) {
            Nodes nodes = nodesService.selectById(id);
            ClusterProviderProxy.INSTANCE.broadcast(new NodeOrderPushCommand(
                    new NodeCommandConfig(nodes.getNodeId().toString(), taskPushState, NodeCommandType.CHANGE_STATUS)));
            return ok(nodes);
        }
        return ok(false);
    }

    @PostMapping("/stoptask")
    @ApiOperation(value = "停止任务", notes = "停止任务")
    public ResponseMessage stopTask(@RequestParam(value = "id", required = true) Long id) throws Exception {
        // System.out.println("停止任务:" + id);
        Nodes nodes = nodesService.selectById(id);
        ClusterProviderProxy.INSTANCE
                .broadcast(new NodeOrderPushCommand(new NodeCommandConfig(nodes.getNodeId().toString(),
                        NodeStatusType.SUSPEND, NodeCommandType.RELEASE_WORK)));
        return ok();
    }
}