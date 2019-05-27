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

package cn.vbill.middleware.porter.core.task.consumer;

import cn.vbill.middleware.porter.core.message.MessageEvent;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.message.converter.EventConverter;
import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.consumer.Position;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 13:36
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 13:36
 */
public abstract class AbstractDataConsumer implements DataConsumer {
    private EventProcessor eventProcessor;
    private EventConverter converter;
    private  volatile MetaQueryClient metaQueryClient;
    protected volatile  ConsumeClient consumeClient;


    @Getter private final List<String> includes = new ArrayList<>();
    @Getter private final List<String> excludes = new ArrayList<>();
    //空查询通知间隔,单位秒
    @Setter @Getter private volatile long emptyFetchNoticeSpan = 30L * 60;

    //空查询通知时间阀值
    @Setter @Getter private volatile long emptyFetchThreshold = -1;

    //初始消费下标
    @Setter private  String offset;

    /**
     * 获取PluginName
     *
     * @date 2018/8/8 下午5:42
     * @param: []
     * @return: java.lang.String
     */
    protected abstract String getPluginName();

    /**
     * doFetch
     *
     * @date 2018/8/8 下午5:42
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.core.message.MessageEvent>
     */
    protected abstract List<MessageEvent> doFetch() throws TaskStopTriggerException, InterruptedException;

    @Override
    public void setExcludes(String exclude) {
        if (!StringUtils.isBlank(exclude)) {
            CollectionUtils.addAll(excludes, exclude.trim().toUpperCase().split(","));
        }
    }

    @Override
    public void setIncludes(String include) {
        if (!StringUtils.isBlank(include)) {
            CollectionUtils.addAll(includes, include.trim().toUpperCase().split(","));
        }
    }



    @Override
    public int getDataCount(String schema, String table, String updateColum, Date startDate, Date endDate) {
        return null != metaQueryClient ? metaQueryClient.getDataCount(schema, table, updateColum, startDate, endDate) : -1;
    }

    @Override
    public boolean isMatch(String consumerName) {
        return getPluginName().equals(consumerName);
    }


    @Override
    public void startup() throws Exception {
        consumeClient.start();
        if (null != metaQueryClient) {
            metaQueryClient.start();
        }
    }

    @Override
    public void shutdown() throws Exception {
        if (!consumeClient.isPublic()) {
            consumeClient.shutdown();
        }
        if (null != metaQueryClient && !metaQueryClient.isPublic()) {
            metaQueryClient.shutdown();
        }
    }

    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {
        consumeClient.initializePosition(taskId, swimlaneId, position);
    }

    /**
     * fetch
     *
     * @date 2018/8/8 下午5:43
     * @param: []
     * @return: java.util.List<cn.vbill.middleware.porter.core.message.MessageEvent>
     */
    public List<MessageEvent> fetch() throws TaskStopTriggerException, InterruptedException {
        return doFetch();
    }

    @Override
    public boolean supportMetaQuery() {
        return null != metaQueryClient;
    }

    @Override
    public String getSwimlaneId() {
        return consumeClient.getSwimlaneId();

    }

    @Override
    public long commitPosition(Position position) throws TaskStopTriggerException {
        return consumeClient.commitPosition(position);
    }

    @Override
    public boolean isAutoCommitPosition() {
        return consumeClient.isAutoCommitPosition();
    }

    @Override
    public EventProcessor getEventProcessor() {
        return eventProcessor;
    }
    @Override
    public void setEventProcessor(EventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
    }

    @Override
    public void setMetaQueryClient(MetaQueryClient c) {
        metaQueryClient = c;
    }

    @Override
    public void setClient(ConsumeClient c) {
        this.consumeClient = c;
    }

    public EventConverter getConverter() {
        return converter;
    }

    @Override
    public void setConverter(EventConverter converter) {
        this.converter = converter;
    }

    @Override
    public String getClientInfo() {
        StringBuffer clientInfo = new StringBuffer();
        if (null != metaQueryClient && metaQueryClient != consumeClient) {
            clientInfo.append("元数据->").append(metaQueryClient.getClientInfo()).append(System.lineSeparator()).append("\t");
        }
        clientInfo.append("消费源->").append(consumeClient.getClientInfo());
        return clientInfo.toString();
    }

    @Override
    public String getInitiatePosition() throws TaskStopTriggerException {
        return consumeClient.getInitiatePosition(offset);
    }


    @Override
    public String getDefaultEventConverter() {
        return null;
    }
}
