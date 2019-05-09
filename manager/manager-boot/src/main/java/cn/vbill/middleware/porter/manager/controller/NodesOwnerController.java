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
import cn.vbill.middleware.porter.manager.service.NodesOwnerService;
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
 * 节点所有权控制表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Api(description = "节点所有权控制表管理")
@RestController
@RequestMapping("/manager/nodesowner")
public class NodesOwnerController {

    @Autowired
    protected NodesOwnerService nodesOwnerService;


    /**
     * 权限设置页面数据组
     *
     * @author MurasakiSeiFu
     * @date 2019-05-07 13:43
     * @param: [jobId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/setPage/{nodeId}")
    @ApiOperation(value = "权限设置页面数据组", notes = "权限设置页面数据组")
    public ResponseMessage nodeOwnerPage(@PathVariable("nodeId") String nodeId) {
        ControlPageVo controlPageVo = nodesOwnerService.makeControlPage(nodeId);
        return ok(controlPageVo);
    }

    /**
     * 权限设置
     *
     * @author MurasakiSeiFu
     * @date 2019-05-07 14:18
     * @param: [controlSettingVo]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping
    @ApiOperation(value = "权限设置", notes = "权限设置")
    public ResponseMessage nodeOwnerSetting(@RequestBody ControlSettingVo controlSettingVo) {
        Integer number = nodesOwnerService.nodeOwnerSetting(controlSettingVo);
        return ok(number);
    }

    /**
     * 回显所有者、共享者
     *
     * @author MurasakiSeiFu
     * @date 2019-05-09 09:08
     * @param: [nodeId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/findNodeOwner/{nodeId}")
    @ApiOperation(value = "回显所有者、共享者", notes = "回显所有者、共享者")
    public ResponseMessage findOwnerByJobId(@PathVariable("nodeId") String nodeId) {
        ControlPageVo controlPageVo = nodesOwnerService.findOwnerByNodeId(nodeId);
        return ok(controlPageVo);
    }
}
