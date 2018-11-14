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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.common.statistics.StatisticData;
import cn.vbill.middleware.porter.common.statistics.TaskPerformance;
import cn.vbill.middleware.porter.manager.service.MrJobTasksMonitorService;
import cn.vbill.middleware.porter.manager.service.MrLogMonitorService;
import cn.vbill.middleware.porter.manager.service.MrNodesMonitorService;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Component
public class TaskPerKafkaListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskPerKafkaListener.class);

    @Autowired
    private MrJobTasksMonitorService mrJobTasksMonitorService;

    @Autowired
    private MrNodesMonitorService mrNodesMonitorService;

    @Autowired
    private MrLogMonitorService mrLogMonitorService;

    @KafkaListener(topics = "${spring.kafka.consumer.topics}")
    public void processMessage(String content) {
        StatisticData statisticData = JSONObject.parseObject(content, StatisticData.class);
        if (statisticData == null) {
            LOGGER.error("Listener-StatisticData-null.....");
            return;
        }
        if (TaskPerformance.NAME.equalsIgnoreCase(statisticData.getCategory())) {
            try {
                LOGGER.info("Listener-TaskPerformance....." + content);
                TaskPerformance taskPerformance = JSONObject.parseObject(content, TaskPerformance.class);
                mrJobTasksMonitorService.dealTaskPerformance(taskPerformance);
                mrNodesMonitorService.dealTaskPerformance(taskPerformance);
            } catch (Exception e) {
                LOGGER.error("Listener-TaskPerformance-Error....出错,请追寻...", e);
            }
        } else if (NodeLog.NAME.equalsIgnoreCase(statisticData.getCategory())) {
            try {
                LOGGER.info("Listener-NodeLog....." + content);
                NodeLog log = JSONObject.parseObject(content, NodeLog.class);
                mrLogMonitorService.dealNodeLog(log);
            } catch (Exception e) {
                LOGGER.error("Listener-NodeLog-Error....出错,请追寻...", e);
            }
        } else {
            LOGGER.error("C-TaskPerKafkaListener...接收到了无法解析数据,内容[{}].", content);
        }

    }
}
