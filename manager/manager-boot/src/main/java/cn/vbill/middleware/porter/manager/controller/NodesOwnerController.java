/**
 * All rights Reserved, Designed By Suixingpay.
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06  
 * @Copyright ©2017 Suixingpay. All rights reserved. 
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.NodesOwner;
import cn.vbill.middleware.porter.manager.service.NodesOwnerService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 节点所有权控制表 controller控制器
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Api(description="节点所有权控制表管理")
@RestController
@RequestMapping("/nodesowner")
public class NodesOwnerController {

    @Autowired
    protected NodesOwnerService nodesOwnerService;

    /**
     * 新增节点owner
     *
     * @param nodesOwner
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody NodesOwner nodesOwner) {
        Integer number = nodesOwnerService.insert(nodesOwner);
        return ok(number);
    }

    /**
     * 修改节点owner
     *
     * @param id
     * @param nodesOwner
     * @return
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody NodesOwner nodesOwner) {
        Integer number = nodesOwnerService.update(id, nodesOwner);
        return ok(number);
    }

    /**
     * 删除节点owner
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        nodesOwnerService.delete(id);
        return ok();
    }

    /**
     * 查询明细
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        NodesOwner nodesOwner = nodesOwnerService.selectById(id);
        return ok(nodesOwner);
    }

    /**
     * 查询列表
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<NodesOwner> page = nodesOwnerService.page(new Page<NodesOwner>(pageNum, pageSize));
        return ok(page);
    }

}
