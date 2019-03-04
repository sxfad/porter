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

import cn.vbill.middleware.porter.manager.core.dto.CRoleMenuVo;
import cn.vbill.middleware.porter.manager.core.entity.CRoleMenu;
import cn.vbill.middleware.porter.manager.service.CRoleMenuService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 角色菜单管理
 *
 * @author: he_xin
 * @date: 2018年12月03日 17:55
 * @version: V1.0
 * @review: he_xin/2018年12月03日 17:55
 */
@Api(description = "角色菜单管理")
@RestController
@RequestMapping("/manager/cRoleMenu")
public class CRoleMenuController {

    @Autowired
    private CRoleMenuService cRoleMenuService;

    /**
     * 添加某一权限能访问的菜单
     *
     * @author he_xin
     * @param cRoleMenuVoList
     * @return
     */
    @PostMapping("/insert")
    @ApiOperation(value = "添加", notes = "添加")
    public ResponseMessage insert(@RequestBody List<CRoleMenuVo> cRoleMenuVoList) {
        cRoleMenuService.insert(cRoleMenuVoList);
        return ok();
    }

    /**
     * 回显权限和能访问的菜单
     *
     * @author he_xin
     * @return
     */
    @GetMapping("/getRoleMenu")
    @ApiOperation(value = "回显权限和能访问的菜单", notes = "回显权限和能访问的菜单")
    public ResponseMessage getRoleMenu() {
        List<CRoleMenu> cRoleMenuList = cRoleMenuService.getRoleMenu();
        return ok(cRoleMenuList);
    }
}
