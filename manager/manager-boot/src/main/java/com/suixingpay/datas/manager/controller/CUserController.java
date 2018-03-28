package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.CUser;
import com.suixingpay.datas.manager.service.CUserService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

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
@RequestMapping("/cuser")
public class CUserController {

    @Autowired
    protected CUserService cuserService;

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody CUser cuser) {
        Integer number = cuserService.insert(cuser);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody CUser cuser) {
        Integer number = cuserService.update(id, cuser);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        cuserService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        CUser cUser = cuserService.selectById(id);
        return ok(cUser);
    }*/

    @ApiOperation(value = "分页列表", notes = "分页列表")
    @GetMapping
    public ResponseMessage page(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<CUser> page = cuserService.page(new Page<CUser>(pageNo, pageSize));
        return ok(page);
    }

    @ApiOperation(value = "全部列表", notes = "全部列表")
    @GetMapping("/list")
    public ResponseMessage list() {
        List<CUser> list = cuserService.list();
        return ok(list);
    }
}