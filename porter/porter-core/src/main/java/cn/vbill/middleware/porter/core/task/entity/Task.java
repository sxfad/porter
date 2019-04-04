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

package cn.vbill.middleware.porter.core.task.entity;

import cn.vbill.middleware.porter.common.task.exception.DataLoaderBuildException;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.task.exception.DataConsumerBuildException;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumerFactory;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import cn.vbill.middleware.porter.core.task.loader.DataLoaderFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 10:12
 */
public class Task {
    private String taskId;
    private List<DataConsumer> consumers;
    private DataLoader loader;
    private List<TableMapper> mappers;
    private List<WarningReceiver> receivers = new ArrayList<>();
    /**
     * 小于1时表示不进行消费进度检查
     */
    @Getter
    @Setter
    private long positionCheckInterval = -1;
    @Getter
    @Setter
    private long alarmPositionCount = 10000;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public DataLoader getLoader() {
        return loader;
    }

    public void setLoader(DataLoader loader) {
        this.loader = loader;
    }

    public List<TableMapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<TableMapper> mappers) {
        this.mappers = mappers;
    }

    public List<DataConsumer> getConsumers() {
        return consumers;
    }

    public void setConsumers(List<DataConsumer> consumers) {
        this.consumers = consumers;
    }

    public List<WarningReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<WarningReceiver> receivers) {
        this.receivers = receivers;
    }

    /**
     * fromConfig
     *
     * @date 2018/8/9 上午9:54
     * @param: [config]
     * @return: cn.vbill.middleware.porter.core.task.entity.Task
     */
    public static Task fromConfig(TaskConfig config) throws ConfigParseException, ClientException, DataLoaderBuildException,
            DataConsumerBuildException {
        Task task = new Task();
        task.setTaskId(config.getTaskId());
        task.setMappers(new ArrayList<>());
        config.getMapper().stream().forEach(m -> {
            task.getMappers().add(TableMapper.fromConfig(m).toUpperCase());
        });
        List<DataConsumer> consumers = DataConsumerFactory.INSTANCE.getConsumer(config.getConsumer());
        task.setConsumers(consumers);
        task.setLoader(DataLoaderFactory.INSTANCE.getLoader(config.getLoader()));
        task.getReceivers().addAll(Arrays.stream(config.getReceiver()).collect(Collectors.toList()));
        task.setAlarmPositionCount(config.getAlarmPositionCount());
        task.setPositionCheckInterval(config.getPositionCheckInterval());
        return task;
    }
}
