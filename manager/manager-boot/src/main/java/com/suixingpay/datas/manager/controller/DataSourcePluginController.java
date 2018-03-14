package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.service.DataSourcePluginService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据源信息关联表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Api(description = "数据源信息关联表管理")
@RestController
@RequestMapping("/datasourceplugin")
public class DataSourcePluginController {

    @Autowired
    protected DataSourcePluginService dataSourcePluginService;
    
    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DataSourcePlugin dataSourcePlugin) {
        Integer number = dataSourcePluginService.insert(dataSourcePlugin);
        return ok(number);
    }
    
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DataSourcePlugin dataSourcePlugin) {
        Integer number = dataSourcePluginService.update(id, dataSourcePlugin);
        return ok(number);
    }
    
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dataSourcePluginService.delete(id);
        return ok();
    }

     @GetMapping("/{id}")
     @ApiOperation(value = "查询明细", notes = "查询明细")
     public ResponseMessage info(@PathVariable("id") Long id) {
         DataSourcePlugin dataSourcePlugin = dataSourcePluginService.selectById(id);
         return ok(dataSourcePlugin);
     }

     @ApiOperation(value = "查询列表", notes = "查询列表")
     @GetMapping
     public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                 @RequestParam(value = "pageSize", required = false) Integer pageSize) {
         Page<DataSourcePlugin> page = dataSourcePluginService.page(new Page<DataSourcePlugin>(pageNo, pageSize));
         return ok(page);
     }*/

}
