package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.service.CUserService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 登陆用户表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "登陆用户表管理")
@RestController
@RequestMapping("/manager/cuser")
public class CUserController {

    @Autowired
    protected CUserService cuserService;

    /**
     * 用户新增
     *
     * @author FuZizheng
     * @date 2018/4/16 上午10:18
     * @param: [cuser]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody CUser cuser) {
        Integer number = cuserService.insert(cuser);
        return ok(number);
    }

    /**
     * 用户修改
     *
     * @author FuZizheng
     * @date 2018/4/16 上午10:20
     * @param: [id, cuser]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody CUser cuser) {
        Integer number = cuserService.update(id, cuser);
        return ok(number);
    }

    /**
     * 验证邮箱或者登录名是否重复
     *
     * @author FuZizheng
     * @date 2018/4/17 下午3:26
     * @param: [loginname, email]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/findByNameOrEmail")
    @ApiOperation(value = "验证邮箱或者登录名是否重复", notes = "参数：登录名、邮箱")
    public ResponseMessage findByNameOrEmail(@RequestParam(value = "loginname", required = false) String loginname,
                                             @RequestParam(value = "email", required = false) String email) {
        boolean flag = cuserService.findByNameOrEmail(loginname, email);
        return ok(flag);
    }

    /**
     * 分页
     *
     * @author FuZizheng
     * @date 2018/4/17 下午3:46
     * @param: [pageNo, pageSize]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @ApiOperation(value = "分页列表", notes = "分页列表")
    @GetMapping
    public ResponseMessage page(@RequestParam(value = "pageNo", required = true) Integer pageNo,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<CUser> page = cuserService.page(new Page<CUser>(pageNo, pageSize));
        return ok(page);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        //cuserService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        CUser cUser = cuserService.selectById(id);
        return ok(cUser);
    }


    @ApiOperation(value = "全部列表", notes = "全部列表")
    @GetMapping("/list")
    public ResponseMessage list() {
        List<CUser> list = cuserService.list();
        return ok(list);
    }
}