/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: hexin[he_xin@suixingpay.com]
 * @date: 2018年12月03日 17时54分
 * @Copyright 2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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
@Api(description = "角色管理")
@RestController
@RequestMapping("/manager/crolemenu")
public class CRoleMenuController {

    @Autowired
    private CRoleMenuService cRoleMenuService;

    /**
     * 添加某一权限能访问的菜单
     *
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
     * @return
     */
    @GetMapping("/getroleMenu")
    @ApiOperation(value = "回显权限和能访问的菜单", notes = "回显权限和能访问的菜单")
    public ResponseMessage getRoleMenu() {
        List<CRoleMenu> cRoleMenuList = cRoleMenuService.getRoleMenu();
        return ok(cRoleMenuList);
    }
}
