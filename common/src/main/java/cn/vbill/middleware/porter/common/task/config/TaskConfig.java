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

package cn.vbill.middleware.porter.common.task.config;

import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 下发任务所需的数据封装
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 18:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 18:10
 */
@NoArgsConstructor
public class TaskConfig {

    public TaskConfig(TaskStatusType status, String taskId, String nodeIds, DataConsumerConfig consumer, DataLoaderConfig loader,
                      List<TableMapperConfig> mapper, WarningReceiver[] receiver) {
        this.status = status;
        this.taskId = taskId;
        this.nodeId = nodeIds;
        this.consumer = consumer;
        this.loader = loader;
        this.mapper = mapper;
        this.receiver = receiver;
    }

    // 根据实际状态 传值
    @Setter
    @Getter
    private TaskStatusType status = TaskStatusType.WORKING;
    // 指定任务由哪些节点执行,","分割 sbkw
    // 这个节点可以不传
    @Setter
    @Getter
    private String nodeId;
    // 任务id 后台固定
    @Setter
    @Getter
    private String taskId;
    // 来源数据
    @Setter
    @Getter
    private DataConsumerConfig consumer;
    // 目标数据
    @Setter
    @Getter
    private DataLoaderConfig loader;
    // 表字段对应关系 TableMapperConfig 实体 == 一张表数据
    @Setter
    @Getter
    private List<TableMapperConfig> mapper = new ArrayList<>();
    // 接收人信息配置
    @Getter
    @Setter
    private WarningReceiver[] receiver = new WarningReceiver[0];

    /**
     * 小于1时表示不进行消费进度检查
     * 单位秒
     */
    @Getter
    @Setter
    private long positionCheckInterval = -1;
    @Getter
    @Setter
    private long alarmPositionCount = 10000;


    /**
     * 是否单例模式下本地任务文件
     */
    @Getter
    @Setter
    private boolean localTask = false;

    /**
     * 是否允许任务出错重启
     */
    @Getter
    @Setter
    private int  restartRetries = 20;

    /**
     * 每次重启失败递增延时
     */
    @Getter
    @Setter
    private int restartIncreaseBySecond = 60 * 5;
}
