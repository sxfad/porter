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


import cn.vbill.middleware.porter.manager.core.entity.DataTable;
import cn.vbill.middleware.porter.manager.core.enums.DataSignEnum;
import cn.vbill.middleware.porter.manager.service.DataTableService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据表信息表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "数据表信息表管理")
@RestController
@RequestMapping("/manager/datatable")
public class DataTableController {

    @Autowired
    private DataTableService dataTableService;

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/3/15 下午5:06
     * @param: []
     * @return: ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DataTable dataTable) {
        Integer number = dataTableService.insert(dataTable);
        return ResponseMessage.ok(number);
    }

    /**
     * 逻辑删除数据表信息
     *
     * @author FuZizheng
     * @date 2018/3/15 下午4:50
     * @param: [id]
     * @return: ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除数据表信息", notes = "数据表主键")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = dataTableService.delete(id);
        return ResponseMessage.ok(number);
    }

    /**
     * 查询明细
     *
     * @author FuZizheng
     * @date 2018/3/15 下午5:25
     * @param: [id]
     * @return: ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DataTable dataTable = dataTableService.selectById(id);
        return ResponseMessage.ok(dataTable);
    }

    /**
     * 查询数据表信息（条件查询）
     *
     * @author FuZizheng
     * @date 2018/3/15 下午4:46
     * @param: [pageNo,
     *             pageSize, bankName, beginTime, endTime]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "bankName", required = false) String bankName,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime) {

        Page<DataTable> page = dataTableService.page(new Page<DataTable>(pageNo, pageSize), bankName, beginTime,
                endTime);
        return ResponseMessage.ok(page, DataSignEnum.DATATABLE);
    }

    /**
     * 数据源下有权限的前缀
     *
     * @author FuZizheng
     * @date 2018/3/19 下午4:52
     * @param: [sourceId]
     * @return: ResponseMessage
     */
    @GetMapping("/prefixs/{sourceId}")
    @ApiOperation(value = "数据源下有权限的前缀", notes = "数据源下有权限的前缀（oracle-用户 or mysql -数据库）")
    public ResponseMessage prefixList(@PathVariable("sourceId") Long sourceId) {
        List<String> prefixs = dataTableService.prefixList(sourceId);
        return ResponseMessage.ok(prefixs);
    }

    /**
     * 数据表名称分页方法
     *
     * @author FuZizheng
     * @date 2018/3/19 下午4:52
     * @param: [pageNo,
     *             pageSize, sourceId, prefix, tableName]
     * @return: ResponseMessage
     */
    @GetMapping("/tables")
    @ApiOperation(value = "数据表名称分页方法", notes = "数据表名称分页方法")
    public ResponseMessage tableList(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "sourceId", required = true) Long sourceId,
            @RequestParam(value = "prefix", required = false) String prefix,
            @RequestParam(value = "tableName", required = false) String tableName) {
        Page<Object> page = dataTableService.tableList(new Page<Object>(pageNo, pageSize), sourceId, prefix, tableName);
        return ResponseMessage.ok(page);
    }

    /**
     * 元数据表组分页方法
     *
     * @author FuZizheng
     * @date 2018/3/26 下午4:55
     * @param: [pageNo,
     *             pageSize]
     * @return: ResponseMessage
     */
    @GetMapping("/datas")
    @ApiOperation(value = "元数据表组分页方法", notes = "元数据表组分页方法")
    public ResponseMessage dataTableList(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<DataTable> page = dataTableService.dataTableList(new Page<DataTable>(pageNo, pageSize));
        return ResponseMessage.ok(page);
    }

    /**
     * 目标数据表组分页方法
     *
     * @author FuZizheng
     * @date 2018/3/27 上午10:29
     * @param: [pageNo,
     *             pageSize]
     * @return: ResponseMessage
     */
    @GetMapping("/targets")
    @ApiOperation(value = "目标数据表组分页方法", notes = "目标数据表组分页方法")
    public ResponseMessage targetList(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize) {
        Page<DataTable> page = dataTableService.dataTableList(new Page<DataTable>(pageNo, pageSize));
        return ResponseMessage.ok(page);
    }
}