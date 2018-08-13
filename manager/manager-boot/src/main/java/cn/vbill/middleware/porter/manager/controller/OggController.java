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

import cn.vbill.middleware.porter.manager.service.OggTablesService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.vbill.middleware.porter.manager.core.entity.OggTables;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Api(description = "OGG信息管理")
@RestController
@RequestMapping("/manager/ogg")
public class OggController {

    private Logger log = LoggerFactory.getLogger(OggController.class);

    @Autowired
    protected OggTablesService oggTablesService;

    /**
     * 接收数据接口
     *
     * @date 2018/8/9 下午4:26
     * @param: [heartbeat, address, tables]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PostMapping("/tables")
    @ApiOperation(value = "接收数据接口", notes = "接收数据接口")
    public ResponseMessage accept(@RequestParam(value = "heartbeat", required = true) String heartbeat,
            @RequestParam(value = "address", required = true) String address,
            @RequestParam(value = "tables", required = true) String tables) {
        log.info("ogg数据接收信息-heartbeat:[{}]" + " address:[{}]" + " tables:[{}]", heartbeat, address, tables);
        oggTablesService.accept(heartbeat, address, tables);
        return ResponseMessage.ok();
    }

    /**
     * 分页
     *
     * @date 2018/8/9 下午4:26
     * @param: [pageNo, pageSize, ipAddress, tableValue]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/tablespage")
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage page(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "ipAddress", required = false) String ipAddress,
            @RequestParam(value = "tableValue", required = false) String tableValue) {
        Page<OggTables> page = oggTablesService.selectPage(new Page<>(pageNo, pageSize), ipAddress, tableValue);
        return ok(page);
    }

}
