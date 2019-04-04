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

import cn.vbill.middleware.porter.manager.core.entity.DataAuthority;
import cn.vbill.middleware.porter.manager.service.DataAuthorityService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 数据权限控制表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
@Api(description = "数据权限控制表管理")
@RestController
@RequestMapping("/manager/dataauthority")
public class DataAuthorityController {

    @Autowired
    protected DataAuthorityService dataAuthorityService;

    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseMessage add(@RequestBody DataAuthority dataAuthority) {
        Integer number = dataAuthorityService.insert(dataAuthority);
        return ok(number);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody DataAuthority dataAuthority) {
        Integer number = dataAuthorityService.update(id, dataAuthority);
        return ok(number);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除", notes = "删除")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        dataAuthorityService.delete(id);
        return ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        DataAuthority dataAuthority = dataAuthorityService.selectById(id);
        return ok(dataAuthority);
    }

    @GetMapping
    @ApiOperation(value = "查询列表", notes = "查询列表")
    public ResponseMessage list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
                                @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Page<DataAuthority> page = dataAuthorityService.page(new Page<DataAuthority>(pageNum, pageSize));
        return ok(page);
    }

}
