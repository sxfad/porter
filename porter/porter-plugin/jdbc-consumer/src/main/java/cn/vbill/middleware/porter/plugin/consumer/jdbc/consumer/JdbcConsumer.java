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

package cn.vbill.middleware.porter.plugin.consumer.jdbc.consumer;


import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.consumer.AbstractDataConsumer;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import cn.vbill.middleware.porter.plugin.connector.jdbc.JdbcConnectorConst;
import cn.vbill.middleware.porter.plugin.consumer.jdbc.JdbcConsumerConst;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
@SuppressWarnings("unchecked")
public class JdbcConsumer extends AbstractDataConsumer {

    public List<MessageEvent> doFetch() throws TaskStopTriggerException, InterruptedException {
        return consumeClient.fetch(new ConsumeClient.FetchCallback<MessageEvent, Object>() {
            @Override
            public <F, O> F accept(O o) {
                //ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                //Position position = new KafkaClient.KafkaPosition(record.topic(), record.offset(), record.partition());
                //return (F) getConverter().convert(record.timestamp(), position, record.value());
                return null;
            }
        });
    }

    @Override
    protected String getPluginName() {
        return JdbcConsumerConst.CONSUMER_PLUGIN_NAME.getCode();
    }

    @Override
    public String getDefaultClientType() {
        return JdbcConnectorConst.CONSUME_SOURCE_TYPE_NAME.getCode();
    }

    @Override
    public String getDefaultMetaClientType() {
        return JdbcConnectorConst.CONSUME_SOURCE_TYPE_NAME.getCode();
    }
}
