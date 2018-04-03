/**
 *
 */
package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.service.DictService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Api(description = "字典数据")
@RestController
@RequestMapping("/manager/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @GetMapping("/all")
    @ApiOperation(value = "全部字典", notes = "全部字典")
    public ResponseMessage dict() {
        Map<String, Map<String, Object>> map = dictService.dictAll();
        return ResponseMessage.ok(map);
    }

    @GetMapping("/{type}")
    @ApiOperation(value = "标识字典", notes = "标识字典")
    public ResponseMessage dictType(@PathVariable("type") String type) {
        Map<String, Object> map = dictService.dictByType(type);
        return ResponseMessage.ok(map);
    }
}
