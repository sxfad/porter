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

import cn.vbill.middleware.porter.manager.core.dto.ControlPageVo;
import cn.vbill.middleware.porter.manager.core.dto.ControlSettingVo;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 任务所有权控制表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Api(description = "任务所有权控制表管理")
@RestController
@RequestMapping("/manager/jobtasksowner")
public class JobTasksOwnerController {

    @Autowired
    protected JobTasksOwnerService jobTasksOwnerService;

    /**
     * 权限设置页面数据
     *
     * @author MurasakiSeiFu
     * @date 2019-04-26 10:09
     * @param: [jobId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/setPage/{jobId}")
    @ApiOperation(value = "权限设置页面数据组", notes = "权限设置页面数据组")
    public ResponseMessage jobOwnerPage(@PathVariable("jobId") Long jobId) {
        ControlPageVo controlPageVo = jobTasksOwnerService.makeControlPage(jobId);
        return ok(controlPageVo);
    }

    /**
     * 权限设置
     *
     * @author MurasakiSeiFu
     * @date 2019-04-04 10:05
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping
    @ApiOperation(value = "权限设置", notes = "权限设置")
    public ResponseMessage jobOwnerSetting(@RequestBody ControlSettingVo controlSettingVo) {
        Integer number = jobTasksOwnerService.jobOwnerSetting(controlSettingVo);
        return ok(number);
    }

}