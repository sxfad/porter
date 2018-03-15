package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.DataSource;
import com.suixingpay.datas.manager.service.DataSourceService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;
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

    /**
     * 新增 数据源信息
     *
     * @author FuZizheng
     * @date 2018/3/13 下午2:23
     * @param: [dataSource]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DataSource dataSource) {
        Integer number = dataSourceService.insert(dataSource);
        return ok(number);
    }

    /**
     * 修改 数据源信息
     *
     * @author FuZizheng
     * @date 2018/3/13 下午3:00
     * @param: [id, dataSource]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改数据源信息", notes = "修改数据源信息")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DataSource dataSource) {
        Integer number = dataSourceService.update(id, dataSource);
        return ok(number);
    }


    /**
     * 逻辑删除数据源信息
     *
     * @author FuZizheng
     * @date 2018/3/13 下午2:50
     * @param: [id]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除数据源信息", notes = "逻辑删除数据源信息")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = dataSourceService.delete(id);
        return ok(number);
    }

    /**
     * 根据数据源id查询关联信息
     *
     * @author FuZizheng
     * @date 2018/3/15 上午10:36
     * @param: [id]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据数据源id查询关联信息", notes = "入参 ： 数据源主键")
    public ResponseMessage findOne(@PathVariable("id") Long id) {
        DataSource dataSource = dataSourceService.selectById(id);
        return ok(dataSource);
    }

    /**
     * 查询数据源信息列表
     *
     * @author FuZizheng
     * @date 2018/3/13 下午1:51
     * @param: [pageNo, pageSize]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage page(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "beginTime", required = false) String beginTime,
                                @RequestParam(value = "endTime", required = false) String endTime) {
         Page<DataSource> page = dataSourceService.page(new Page<DataSource>(pageNo, pageSize), name, beginTime, endTime);
        return ok(page);
    }

    /*@PostMapping
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
    }*/

}