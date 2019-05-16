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

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

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

import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.core.enums.DataSignEnum;
import cn.vbill.middleware.porter.manager.service.DataSourceService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/manager/datasource")
public class DataSourceController {

    @Autowired
    protected DataSourceService dataSourceService;

    /**
     * 新增 数据源信息
     *
     * @author FuZizheng
     * @date 2018/3/13 下午2:23
     * @param: [dataSource]
     * @return: ResponseMessage
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
     * @param: [id,
     * dataSource]
     * @return: ResponseMessage
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
     * @return: ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除数据源信息", notes = "逻辑删除数据源信息")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = dataSourceService.delete(id);
        return ok(number);
    }

    /**
     * 消费数据来源
     *
     * @author FuZizheng
     * @date 2018/3/26 下午5:20
     * @param: []
     * @return: ResponseMessage
     */
    @GetMapping("/findByType")
    @ApiOperation(value = "消费数据来源", notes = "消费数据来源")
    public ResponseMessage findByType(@RequestParam(value = "pageNo", required = true) Integer pageNo,
                                      @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<DataSource> page = dataSourceService.findByTypePage(new Page<DataSource>(pageNo, pageSize));
        return ok(page);
    }

    /**
     * 根据数据源id查询关联信息
     *
     * @author FuZizheng
     * @date 2018/3/15 上午10:36
     * @param: [id]
     * @return: ResponseMessage
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
     * @param: [pageNo,
     * pageSize]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage page(@RequestParam(value = "pageNo", required = true) Integer pageNo,
                                @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "beginTime", required = false) String beginTime,
                                @RequestParam(value = "endTime", required = false) String endTime,
                                @RequestParam(value = "dataType", required = false) String dataType) {
        Page<DataSource> page = dataSourceService.page(new Page<DataSource>(pageNo, pageSize), name, beginTime,
                endTime, dataType);
        return ok(page, DataSignEnum.DATASOURCE);
    }


}