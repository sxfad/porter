/**
 * 
 */
package com.suixingpay.datas.manager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.manager.web.message.ResponseMessage;

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

    @PostMapping("/tables")
    @ApiOperation(value = "接收数据接口", notes = "接收数据接口")
    public ResponseMessage accept(@RequestParam(value = "hearthead", required = true) String hearthead,
            @RequestParam(value = "ip", required = true) String ip,
            @RequestParam(value = "tables", required = true) String tables) {
        log.info("hearthead:[{}]" + " ip:[{}]" + " tables:[{}]", hearthead, ip, tables);
        return ResponseMessage.ok();
    }

}
