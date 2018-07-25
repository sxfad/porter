/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

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

}