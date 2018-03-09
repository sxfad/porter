package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 数据源信息表 controller控制器
 * 
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "数据源信息表管理")
@RestController
@RequestMapping("/datasource")
public class DataSourceController {

    @Autowired
    protected DataSourceService dataSourceService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DataSource dataSource) {
        Integer number = dataSourceService.insert(dataSource);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DataSource dataSource) {
        Integer number = dataSourceService.update(id, dataSource);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dataSourceService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DataSource dataSource = dataSourceService.selectById(id);
        return ok(dataSource);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<DataSource> page = dataSourceService.page(new Page<DataSource>(pageNo, pageSize));
        return ok(page);
    }

}