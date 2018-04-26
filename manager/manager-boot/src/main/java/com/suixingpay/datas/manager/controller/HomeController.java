/**
 *
 */
package com.suixingpay.datas.manager.controller;

import com.suixingpay.datas.manager.core.icon.BlockMessage;
import com.suixingpay.datas.manager.core.icon.HomeBlock;
import com.suixingpay.datas.manager.core.icon.HomeBlockResult;
import com.suixingpay.datas.manager.service.HomeService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
@Api(description = "首页待办项")
@RestController
@RequestMapping("/manager/home")
public class HomeController {

    @Autowired
    private HomeService homeService;

    @GetMapping("/blocks")
    @ApiOperation(value = "首页事项", notes = "首页事项")
    public ResponseMessage blocks() {
        HomeBlockResult homeBlockResult = homeService.bolck();
        List<HomeBlock> blocks = new ArrayList<>();
        HomeBlock homeBlock1 = new HomeBlock("平台监控事项",
                Arrays.asList(new BlockMessage("1", "十分钟内日志异常(" + homeBlockResult.getTenMinutesCount() + ")条！", "/logMonitor"),
                        new BlockMessage("2", "一小时内日志异常(" + homeBlockResult.getOneHourCount() + ")条！", "/logMonitor"),
                        new BlockMessage("3", "24小时内日志异常(" + homeBlockResult.getTwentyFourHourCount() + ")条！", "/logMonitor")));
        blocks.add(homeBlock1);
        HomeBlock homeBlock2 = new HomeBlock("平台任务事项", Arrays.asList(new BlockMessage("1", "运行中任务(" + homeBlockResult.getTasksWorkingCount() + ")条！", "/synchTask")));
        blocks.add(homeBlock2);
        return ResponseMessage.ok(blocks);
    }
}
