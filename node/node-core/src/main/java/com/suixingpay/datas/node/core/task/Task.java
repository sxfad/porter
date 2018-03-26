/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.task;

import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.config.TaskConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.exception.DataConsumerBuildException;
import com.suixingpay.datas.common.exception.DataLoaderBuildException;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.consumer.DataConsumerFactory;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.loader.DataLoaderFactory;

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
    private List<AlertReceiver> receivers = new ArrayList<>();

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

    public List<AlertReceiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<AlertReceiver> receivers) {
        this.receivers = receivers;
    }

    public static Task  fromConfig(TaskConfig config) throws ConfigParseException, ClientException, DataLoaderBuildException,
            DataConsumerBuildException {
        Task task = new Task();
        task.setTaskId(config.getTaskId());
        task.setMappers(new ArrayList<>());
        config.getMapper().stream().filter(m -> !m.isAuto()).forEach(m -> {
            task.getMappers().add(TableMapper.fromConfig(m).toUpperCase());
        });
        List<DataConsumer> consumers = DataConsumerFactory.INSTANCE.getConsumer(config.getConsumer());
        task.setConsumers(consumers);
        task.setLoader(DataLoaderFactory.INSTANCE.getLoader(config.getLoader()));
        task.getReceivers().addAll(Arrays.stream(config.getReceiver()).collect(Collectors.toList()));
        return task;
    }


}
