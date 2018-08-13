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

import cn.vbill.middleware.porter.manager.service.DataFieldService;
import cn.vbill.middleware.porter.manager.core.entity.DataField;
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

import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 数据字段对应表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "数据字段对应表管理")
@RestController
@RequestMapping("/manager/datafield")
public class DataFieldController {

    @Autowired
    protected DataFieldService dataFieldService;

    /**
     * add
     *
     * @date 2018/8/9 下午4:20
     * @param: [dataField]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody DataField dataField) {
        Integer number = dataFieldService.insert(dataField);
        return ok(number);
    }

    /**
     * 修改
     *
     * @date 2018/8/9 下午4:20
     * @param: [id, dataField]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DataField dataField) {
        Integer number = dataFieldService.update(id, dataField);
        return ok(number);
    }

    /**
     * 删除
     *
     * @date 2018/8/9 下午4:20
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dataFieldService.delete(id);
        return ok();
    }

    /**
     * 查询明细
     *
     * @date 2018/8/9 下午4:20
     * @param: [id]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DataField dataField = dataFieldService.selectById(id);
        return ok(dataField);
    }

    /**
     * 查询列表
     *
     * @date 2018/8/9 下午4:20
     * @param: [pageNo, pageSize]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @ApiOperation(value = "查询列表", notes = "查询列表")
    @GetMapping
    public ResponseMessage list(@RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<DataField> page = dataFieldService.page(new Page<DataField>(pageNo, pageSize));
        return ok(page);
    }

}