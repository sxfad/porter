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

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.command.LogConfigPushCommand;
import cn.vbill.middleware.porter.common.config.LogConfig;
import cn.vbill.middleware.porter.manager.core.entity.LogGrade;
import cn.vbill.middleware.porter.manager.service.LogGradeService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return: ResponseMessage
     * @throws Exception
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
    public ResponseMessage add(@RequestBody LogGrade logGrade) throws Exception {
        Integer number = logGradeService.insert(logGrade);
        if (number == 1) {
            ClusterProviderProxy.INSTANCE
                    .broadcastEvent(new LogConfigPushCommand(new LogConfig(logGrade.getLogLevel().getCode())));
        }
        return ResponseMessage.ok(number);
    }

    /**
     * 查询
     *
     * @author FuZizheng
     * @date 2018/3/16 上午11:30
     * @param: []
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询当前日志级别", notes = "若无数据，默认为INFO")
    public ResponseMessage info() {
        LogGrade logGrade = logGradeService.select();
        return ResponseMessage.ok(logGrade);
    }

}