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

import cn.vbill.middleware.porter.manager.service.OwnerControlService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 权限控制操作类型表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
@Api(description = "权限控制操作类型表管理")
@RestController
@RequestMapping("/manager/ownercontrol")
public class OwnerControlController {

    @Autowired
    protected OwnerControlService ownerControlService;

//    /**
//     * 查询全部对应关系
//     *
//     * @author MurasakiSeiFu
//     * @date 2019-04-03 14:52
//     * @param: []
//     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
//     */
//    @GetMapping("/findAll")
//    @ApiOperation(value = "查询全部对应关系", notes = "查询全部对应关系")
//    public ResponseMessage findAll() {
//        List<OwnerControl> list = ownerControlService.findAll(null);
//        return ok(list);
//    }
//
//    /**
//     * 根据权限类型查询
//     *
//     * @author MurasakiSeiFu
//     * @date 2019-04-03 14:56
//     * @param: []
//     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
//     */
//    @GetMapping("/findByType")
//    @ApiOperation(value = "根据权限类型查询", notes = "根据权限类型查询")
//    public ResponseMessage findByType(@RequestParam(value = "type", required = true) Integer type) {
//        List<OwnerControl> list = ownerControlService.findAll(type);
//        return ok(list);
//    }
}
