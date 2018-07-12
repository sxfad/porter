/**
 *
 */
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.manager.core.icon.HomeBlock;
import cn.vbill.middleware.porter.manager.core.icon.BlockMessage;
import cn.vbill.middleware.porter.manager.core.icon.HomeBlockResult;
import cn.vbill.middleware.porter.manager.service.HomeService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
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
        HomeBlockResult l = homeService.bolck();
        List<HomeBlock> blocks = new ArrayList<>();

        // 任务监控项
        HomeBlock homeBlock2 = new HomeBlock("任务监控项",
                Arrays.asList(new BlockMessage("1", "运行中任务(" + l.getTasksWorkingCount() + ")条！", "/taskMonitor")));
        blocks.add(homeBlock2);

        // 节点监控项
        HomeBlock homeBlock3 = new HomeBlock("节点监控项",
                Arrays.asList(
                        new BlockMessage("1", "在线节点(" + l.getNodeNum1() + ")个,其中(" + l.getNodeNum2() + ")个运行中！",
                                "/nodeCluster"),
                        new BlockMessage("2", "节点健康状况:正常(" + l.getMrNodeNum1() + ")个,需关注(" + l.getMrNodeNum2()
                                + ")个,异常(" + l.getMrNodeNum3() + ")个！", "/nodeMonitor")));
        blocks.add(homeBlock3);

        // 日志监控项
        HomeBlock homeBlock1 = new HomeBlock("日志监控项",
                Arrays.asList(new BlockMessage("1", "十分钟内日志异常(" + l.getTenMinutesCount() + ")条！", "/logMonitor"),
                        new BlockMessage("2", "一小时内日志异常(" + l.getOneHourCount() + ")条！", "/logMonitor"),
                        new BlockMessage("3", "24小时内日志异常(" + l.getTwentyFourHourCount() + ")条！", "/logMonitor")));
        blocks.add(homeBlock1);

        return ResponseMessage.ok(blocks);
    }
}
