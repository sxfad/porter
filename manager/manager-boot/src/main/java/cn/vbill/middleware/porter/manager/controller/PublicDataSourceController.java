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

import cn.vbill.middleware.porter.manager.core.entity.PublicDataSource;
import cn.vbill.middleware.porter.manager.service.PublicDataSourceService;
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
 * 公共数据源配置表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-13 09:58:24
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-13 09:58:24
 */
@Api(description = "公共数据源配置表管理")
@RestController
@RequestMapping("/publicdatasource")
public class PublicDataSourceController {

    @Autowired
    protected PublicDataSourceService publicDataSourceService;

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [publicDataSource]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.insert(publicDataSource);
        return ok(number);
    }

    /**
     * 修改
     *
     * @author FuZizheng
     * @date ` 10:14
     * @param: [id, publicDataSource]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody PublicDataSource publicDataSource) {
        Integer number = publicDataSourceService.update(id, publicDataSource);
        return ok(number);
    }

    /**
     * 删除
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        publicDataSourceService.delete(id);
        return ok();
    }

    /**
     * 查询明细
     *
     * @author FuZizheng
     * @date 2019-03-13 10:14
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        PublicDataSource publicDataSource = publicDataSourceService.selectById(id);
        return ok(publicDataSource);
    }

    /**
     * 插叙列表
     *
     * @author FuZizheng
     * @date 10:14
     * @param: [pageNum, pageSize]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = true) Integer pageNum,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<PublicDataSource> page = publicDataSourceService.page(new Page<PublicDataSource>(pageNum, pageSize));
        return ok(page);
    }

}
