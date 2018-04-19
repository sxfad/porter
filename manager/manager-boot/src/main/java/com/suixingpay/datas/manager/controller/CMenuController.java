package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.CMenu;
import com.suixingpay.datas.manager.service.CMenuService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import java.util.List;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: 付紫钲
 * @date: 2018/4/16
 */
@Api(description = "菜单管理")
@RestController
@RequestMapping("/manager/cmenu")
public class CMenuController {

    @Autowired
    private CMenuService cMenuService;

    /**
     * 查询全部菜单
     *
     * @author FuZizheng
     * @date 2018/4/16 下午2:31
     * @param: [roleCode]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询全部菜单", notes = "查询全部菜单")
    public ResponseMessage findAll() {
        CMenu cMenu = cMenuService.findAll();
        return ok(cMenu);
    }

    /**
     * 根据fatherCode查询
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:21
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/findByFatherCode")
    @ApiOperation(value = "根据fatherCode查询", notes = "根据fatherCode查询")
    public ResponseMessage findByFatherCode(@RequestParam(value = "fatherCode", required = false) String fatherCode) {
        List<CMenu> menus = cMenuService.findByFatherCode(fatherCode);
        return ok(menus);
    }

    /**
     * 根据主键查询
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:43
     * @param: [id]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据主键查询", notes = "根据主键查询")
    public ResponseMessage findById(@PathVariable(value = "id", required = false) Long id) {
        CMenu cMenu = cMenuService.findById(id);
        return ok(cMenu);
    }

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:27
     * @param: [cMenu]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage insert(@RequestBody CMenu cMenu) {
        Integer number = cMenuService.insert(cMenu);
        return ok(number);
    }

    /**
     * 修改
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:41
     * @param: [id, cMenu]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody CMenu cMenu) {
        Integer number = cMenuService.update(id, cMenu);
        return ok(number);
    }

    /**
     * 逻辑删除
     *
     * @author FuZizheng
     * @date 2018/4/16 下午3:51
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除", notes = "逻辑删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = cMenuService.delete(id);
        return ok(number);
    }

}
