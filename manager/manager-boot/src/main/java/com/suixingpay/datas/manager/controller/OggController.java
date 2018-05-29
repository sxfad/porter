/**
 * 
 */
package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.core.entity.OggTables;
import com.suixingpay.datas.manager.service.OggTablesService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;

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

    @PostMapping("/tables")
    @ApiOperation(value = "接收数据接口", notes = "接收数据接口")
    public ResponseMessage accept(@RequestParam(value = "heartbeat", required = true) String heartbeat,
            @RequestParam(value = "ipaddress", required = true) String ipaddress,
            @RequestParam(value = "tables", required = true) String tables) {
        log.info("ogg数据接收信息-heartbeat:[{}]" + " ipaddress:[{}]" + " tables:[{}]", heartbeat, ipaddress, tables);
        oggTablesService.accept(heartbeat, ipaddress, tables);
        return ResponseMessage.ok();
    }

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
