package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.CRole;
import cn.vbill.middleware.porter.manager.service.CRoleService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 付紫钲
 * @date: 2018/4/16
 * @copyright: ©2017 Suixingpay. All rights reserved. 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
@Api(description = "角色管理")
@RestController
@RequestMapping("/manager/crole")
public class CRoleController {

    @Autowired
    private CRoleService cRoleService;

    /**
     * 用户新增 角色下拉接口
     *
     * @author FuZizheng
     * @date 2018/4/16 上午10:23
     * @param: []
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "角色列表接口", notes = "角色列表接口")
    public ResponseMessage findAList() {
        List<CRole> roles = cRoleService.findList();
        return ok(roles);
    }
}
