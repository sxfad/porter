package com.suixingpay.datas.manager.controller;


import com.suixingpay.datas.manager.ManagerContext;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 告警配置表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:46:01
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:46:01
 */
@Api(description = "zabbix任务报警接口")
@RestController
@RequestMapping("/alarm/task")
public class TaskStopedController {
    @GetMapping("/check")
    public String info() {
        Map<String, List<String>> tasks = ManagerContext.INSTANCE.getStoppedTasks();
        return tasks.isEmpty() ? StringUtils.EMPTY : tasks.toString();
    }
}