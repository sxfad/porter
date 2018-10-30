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
package cn.vbill.middleware.porter.manager.cluster.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.statistics.TaskPerformance;
import cn.vbill.middleware.porter.manager.service.MrJobTasksMonitorService;
import cn.vbill.middleware.porter.manager.service.MrNodesMonitorService;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Component
public class TaskPerKafkaListener {

    @Autowired
    private MrJobTasksMonitorService mrJobTasksMonitorService;

    @Autowired
    private MrNodesMonitorService mrNodesMonitorService;

    @KafkaListener(topics = "${spring.kafka.consumer.topics}")
    public void processMessage(String content) {

        TaskPerformance performance = JSONObject.parseObject(content, TaskPerformance.class);
        // 任务泳道实时监控表 服务接口类
        mrJobTasksMonitorService.dealTaskPerformance(performance);
        // 节点任务实时监控表
        mrNodesMonitorService.dealTaskPerformance(performance);
    }
}
