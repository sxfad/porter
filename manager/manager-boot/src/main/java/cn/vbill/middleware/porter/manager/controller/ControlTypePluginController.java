/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.ControlTypePlugin;
import cn.vbill.middleware.porter.manager.service.ControlTypePluginService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
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

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 操作类型字典 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
@Api(description = "操作类型字典管理")
@RestController
@RequestMapping("/controltypeplugin")
public class ControlTypePluginController {

    @Autowired
    protected ControlTypePluginService controlTypePluginService;


    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody ControlTypePlugin controlTypePlugin) {
        Integer number = controlTypePluginService.insert(controlTypePlugin);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody ControlTypePlugin controlTypePlugin) {
        Integer number = controlTypePluginService.update(id, controlTypePlugin);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        controlTypePluginService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        ControlTypePlugin controlTypePlugin = controlTypePluginService.selectById(id);
        return ok(controlTypePlugin);
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<ControlTypePlugin> page = controlTypePluginService.page(new Page<ControlTypePlugin>(pageNum, pageSize));
        return ok(page);
    }

}
