/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 13:36
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.consumer;

import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.EventProcessor;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import lombok.Getter;
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
    protected EventConverter converter;
    private MetaQueryClient metaQueryClient;
    protected ConsumeClient consumeClient;


    @Getter private final List<String> includes = new ArrayList<>();
    @Getter private final List<String> excludes = new ArrayList<>();

    @Override
    public void setExcludes(String exclude) {
        if (!StringUtils.isBlank(exclude)) {
            CollectionUtils.addAll(excludes, exclude.trim().toUpperCase().split(","));
            excludes.forEach(k -> k.trim());
        }
    }

    @Override
    public void setIncludes(String include) {
        if (!StringUtils.isBlank(include)) {
            CollectionUtils.addAll(includes, include.trim().toUpperCase().split(","));
            includes.forEach(k -> k.trim());
        }
    }

    @Override
    public void setMetaQueryClient(MetaQueryClient c) {
        metaQueryClient = c;
    }

    @Override
    public void setClient(ConsumeClient c) {
        this.consumeClient = c;
    }

    @Override
    public void setConverter(EventConverter converter) {
        this.converter = converter;
    }

    @Override
    public int getDataCount(String schema, String table, String updateColum, Date startDate, Date endDate) {
        return metaQueryClient.getDataCount(schema, table, updateColum, startDate, endDate);
    }

    @Override
    public boolean isMatch(String consumerName) {
        return getPluginName().equals(consumerName);
    }

    protected abstract String getPluginName();

    @Override
    public void shutdown() throws Exception {
        if (!consumeClient.isPublic()) consumeClient.shutdown();
        if (!metaQueryClient.isPublic()) metaQueryClient.shutdown();
    }

    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {
        consumeClient.initializePosition(taskId, swimlaneId, position);
    }

    @Override
    public void startup() throws Exception {
        consumeClient.start();
        metaQueryClient.start();
    }

    public List<MessageEvent> fetch() {
        return doFetch();
    }

    protected abstract List<MessageEvent> doFetch();


    @Override
    public String getSwimlaneId() {
        return consumeClient.getSwimlaneId();

    }

    @Override
    public void commitPosition(String position) throws TaskStopTriggerException {
        consumeClient.commitPosition(position);
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
}
