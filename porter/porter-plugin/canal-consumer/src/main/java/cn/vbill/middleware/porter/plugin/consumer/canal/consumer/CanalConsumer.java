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

package cn.vbill.middleware.porter.plugin.consumer.canal.consumer;


import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.consumer.AbstractDataConsumer;
import cn.vbill.middleware.porter.plugin.consumer.canal.CanalConsumerConst;
import cn.vbill.middleware.porter.plugin.consumer.canal.client.CanalClient;
import cn.vbill.middleware.porter.plugin.converter.canal.CanalRowConverter;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * canal row消费端
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月06日 11:27
 * @version: V2.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月06日 11:27
 */
@SuppressWarnings("unchecked")
public class CanalConsumer extends AbstractDataConsumer {
    public List<MessageEvent> doFetch() throws TaskStopTriggerException, InterruptedException {
        return consumeClient.fetch(new ConsumeClient.FetchCallback<MessageEvent, Object>() {
            @Override
            public <F, O> List<F> acceptAll(O o) throws TaskStopTriggerException {
                List<MessageEvent> events = new ArrayList<>();
                Message msg = (Message) o;

                List<CanalEntry.Entry> entries;
                if (msg.isRaw()) {
                    entries =  new ArrayList<>();
                    for (ByteString e : msg.getRawEntries()) {
                        try {
                            entries.add(CanalEntry.Entry.parseFrom(e));
                        } catch (InvalidProtocolBufferException ex) {
                            throw new TaskStopTriggerException(ex);
                        }
                    }
                } else {
                    entries = msg.getEntries();
                }

                //批次消息同步提交点
                CanalClient.CanalPosition bucketHeader = null;
                List<CanalEntry.Entry> endEntries = entries.stream()
                        .filter(e -> e.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND || e.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN)
                        .collect(Collectors.toList());
                if (!endEntries.isEmpty()) {
                    CanalEntry.Entry lastEndEntry = endEntries.get(endEntries.size() - 1);
                    bucketHeader = new CanalClient.CanalPosition(msg.getId(), lastEndEntry.getHeader().getLogfileOffset(),
                            lastEndEntry.getHeader().getLogfileName());
                } else {
                    bucketHeader = new CanalClient.CanalPosition(msg.getId());
                }

                for (CanalEntry.Entry entry : entries) {
                    //事务消息同步点
                    CanalClient.CanalPosition rowHeader = new CanalClient.CanalPosition(msg.getId(), entry.getHeader().getLogfileOffset(),
                            entry.getHeader().getLogfileName());

                    List<MessageEvent> convertedObj = getConverter().convertList(bucketHeader, rowHeader, entry);
                    if (null != convertedObj && !convertedObj.isEmpty()) {
                        events.addAll(convertedObj);
                    }
                }
                return (List<F>) events;
            }
        });
    }

    @Override
    protected String getPluginName() {
        return CanalConsumerConst.CONSUMER_PLUGIN_NAME.getCode();
    }

    @Override
    public String getDefaultClientType() {
        return CanalConsumerConst.CONSUMER_SOURCE_TYPE_NAME.getCode();
    }

    @Override
    public String getDefaultMetaClientType() {
        return CanalConsumerConst.CONSUMER_SOURCE_TYPE_NAME.getCode();
    }

    @Override
    public String getDefaultEventConverter() {
        return CanalRowConverter.CONVERTER_NAME;
    }
}
