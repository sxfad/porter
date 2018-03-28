package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.entity.DicDataSourcePlugin;
import com.suixingpay.datas.manager.service.DicDataSourcePluginService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

/**
 * 数据源信息字典表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-14 13:54:16
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-14 13:54:16
 */
@Api(description = "数据源信息字典表管理")
@RestController
@RequestMapping("/dicdatasourceplugin")
public class DicDataSourcePluginController {

    @Autowired
    protected DicDataSourcePluginService dicDataSourcePluginService;

    /**
     * 根据数据源类型查询页面字段
     *
     * @author FuZizheng
     * @date 2018/3/14 下午5:12
     * @param: [sourceType]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping("/{sourceType}")
    @ApiOperation(value = "查询页面字段", notes = "入参：sourceType")
    public ResponseMessage findByType(@PathVariable("sourceType") String sourceType) {
        List<DicDataSourcePlugin> dicDataSourcePluginList = dicDataSourcePluginService.findByType(sourceType);
        return ok(dicDataSourcePluginList);
    }

    /*@PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DicDataSourcePlugin dicDataSourcePlugin) {
        Integer number = dicDataSourcePluginService.insert(dicDataSourcePlugin);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DicDataSourcePlugin dicDataSourcePlugin) {
        Integer number = dicDataSourcePluginService.update(id, dicDataSourcePlugin);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dicDataSourcePluginService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DicDataSourcePlugin dicDataSourcePlugin = dicDataSourcePluginService.selectById(id);
        return ok(dicDataSourcePlugin);
    }

    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<DicDataSourcePlugin> page = dicDataSourcePluginService.page(new Page<DicDataSourcePlugin>(pageNo, pageSize));
        return ok(page);
    }*/

}
