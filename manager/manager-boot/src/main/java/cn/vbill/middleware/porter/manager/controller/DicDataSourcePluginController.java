package cn.vbill.middleware.porter.manager.controller;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.DicDataSourcePlugin;
import cn.vbill.middleware.porter.manager.service.DicDataSourcePluginService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/manager/dicdatasourceplugin")
public class DicDataSourcePluginController {

    @Autowired
    protected DicDataSourcePluginService dicDataSourcePluginService;

    /**
     * 根据数据源类型查询页面字段
     *
     * @author FuZizheng
     * @date 2018/3/14 下午5:12
     * @param: [sourceType]
     * @return: ResponseMessage
     */
    @GetMapping("/{sourceType}")
    @ApiOperation(value = "查询页面字段", notes = "入参：sourceType")
    public ResponseMessage findByType(@PathVariable("sourceType") String sourceType) {
        List<DicDataSourcePlugin> dicDataSourcePluginList = dicDataSourcePluginService.findByType(sourceType);
        return ResponseMessage.ok(dicDataSourcePluginList);
    }
}
