/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.entity.DicDataSourcePlugin;
import cn.vbill.middleware.porter.manager.service.DicDataSourcePluginService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
