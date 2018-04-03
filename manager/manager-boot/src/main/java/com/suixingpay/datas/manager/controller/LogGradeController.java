package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.LogConfigPushCommand;
import com.suixingpay.datas.common.config.LogConfig;
import com.suixingpay.datas.manager.core.entity.LogGrade;
import com.suixingpay.datas.manager.service.LogGradeService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 日志级别表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "日志级别表管理")
@RestController
@RequestMapping("/manager/loggrade")
public class LogGradeController {

    @Autowired
    protected LogGradeService logGradeService;

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/3/16 上午11:03
     * @param: [logGrade]
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     * @throws Exception
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody LogGrade logGrade) throws Exception {
        Integer number = logGradeService.insert(logGrade);
        if (number == 1) {
            ClusterProviderProxy.INSTANCE
                    .broadcast(new LogConfigPushCommand(new LogConfig(logGrade.getLogLevel().getCode())));
        }
        return ok(number);
    }

    /**
     * 查询
     *
     * @author FuZizheng
     * @date 2018/3/16 上午11:30
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询当前日志级别", notes = "若无数据，默认为INFO")
    public ResponseMessage info() {
        LogGrade logGrade = logGradeService.select();
        return ok(logGrade);
    }

}