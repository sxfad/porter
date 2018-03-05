/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.consumer.kafka;


import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.node.core.consumer.AbstractDataConsumer;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.text.ParseException;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
public class KafkaConsumer extends AbstractDataConsumer {
    private static final String CONSUMER_NAME = "KafkaFetch";

    public List<MessageEvent> doFetch() {
        return consumeClient.fetch(new ConsumeClient.FetchCallback<MessageEvent, Object>() {
            @Override
            public <F, O> F accept(O o) throws ParseException {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                JSONObject value = JSONObject.parseObject(record.value());
                JSONObject position = new JSONObject();
                position.put("offset", record.offset());
                position.put("topic", record.topic());
                position.put("partition", record.partition());
                return (F) converter.convert(position, value);
            }
        });
    }

    @Override
    protected String getPluginName() {
        return CONSUMER_NAME;
    }
}
