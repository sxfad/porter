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

import cn.vbill.middleware.porter.manager.core.icon.MrJobMonitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.vbill.middleware.porter.manager.service.MrJobTasksMonitorService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 任务泳道实时监控表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "任务泳道实时监控表管理")
@RestController
@RequestMapping("/manager/mrjobtasksmonitor")
public class MrJobTasksMonitorController {

    @Autowired
    protected MrJobTasksMonitorService mrJobTasksMonitorService;

    /**
     * 任务泳道实时数据
     *
     * @date 2018/8/9 下午4:25
     * @param: [jobId, swimlaneId, schemaTable, intervalTime, intervalCount]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @ApiOperation(value = "任务泳道实时数据(按分)", notes = "任务泳道实时数据(按分)")
    @GetMapping("/jobmonitor")
    public ResponseMessage jobMonitor(@RequestParam(value = "jobId", required = true) String jobId,
            @RequestParam(value = "swimlaneId", required = true) String swimlaneId,
            @RequestParam(value = "schemaTable", required = true) String schemaTable,
            @RequestParam(value = "intervalTime", required = true) Long intervalTime,
            @RequestParam(value = "intervalCount", required = true) Long intervalCount) {
        MrJobMonitor mrJobMonitor = mrJobTasksMonitorService.obMrJobMonitor(jobId, swimlaneId, schemaTable,
                intervalTime, intervalCount);
        return ResponseMessage.ok(mrJobMonitor);
    }

    /**
     * 任务泳道实时数据
     *
     * @param jobId
     * @param swimlaneId
     * @param schemaTable
     * @param date
     * @param intervalTime
     * @param intervalCount
     * @return
     */
    @ApiOperation(value = "任务泳道实时数据(按分)", notes = "任务泳道实时数据(按分)")
    @GetMapping("/jobMonitorDetail")
    public ResponseMessage jobMonitorDetail(@RequestParam(value = "jobId", required = true) String jobId,
                                            @RequestParam(value = "swimlaneId", required = true) String swimlaneId,
                                            @RequestParam(value = "schemaTable", required = true) String schemaTable,
                                            @RequestParam(value = "monitorDate", required = true) String date,
                                            @RequestParam(value = "intervalTime", required = false) Long intervalTime,
                                            @RequestParam(value = "intervalCount", required = true) Long intervalCount) {
        MrJobMonitor mrJobMonitor = mrJobTasksMonitorService.obMrJobMonitorDetail(jobId, swimlaneId, schemaTable, date, intervalTime, intervalCount);
        return ResponseMessage.ok(mrJobMonitor);
    }
}