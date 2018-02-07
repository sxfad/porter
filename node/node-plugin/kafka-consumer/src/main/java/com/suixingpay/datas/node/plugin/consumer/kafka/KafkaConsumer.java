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
import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.client.ClientCallback;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.client.impl.KafkaClient;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
public class KafkaConsumer implements DataConsumer {
    private static final String CONSUMER_NAME = "KafkaFetch";
    private MetaQueryClient metaQueryClient;
    private KafkaClient consumeClient;
    private EventConverter converter;

    @Override
    public void setConverter(EventConverter converter) {
        this.converter = converter;
    }

    @Override
    public <C extends ConsumeClient> void setClient(C c) {
        this.consumeClient = (KafkaClient)c;
    }

    @Override
    public <C extends MetaQueryClient> void setMetaQueryClient(C c) {
        metaQueryClient = c;
    }

    @Override
    public boolean isMatch(String consumerName) {
        return CONSUMER_NAME.equals(consumerName);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void shutdown() throws InterruptedException {
        if (!consumeClient.isPublic()) consumeClient.shutdown();
        if (metaQueryClient instanceof Client && !((Client)metaQueryClient).isPublic()) ((Client)metaQueryClient).shutdown();
    }

    @Override
    public void startup() throws IOException {
        consumeClient.start();
        if (metaQueryClient instanceof Client) ((Client) metaQueryClient).start();
    }

    @Override
    public List<MessageEvent> fetch() {
        return consumeClient.fetch(new ClientCallback<MessageEvent, Object>() {
            @Override
            public <F, O> F accept(O o) throws ParseException {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                JSONObject value = JSONObject.parseObject(record.value());
                JSONObject head = new JSONObject();
                head.put("offset", record.offset());
                head.put("topic", record.topic());
                head.put("partition", record.partition());
                head.put("key", record.key());
                return (F) converter.convert(head, value);
            }
        });
    }

    @Override
    public int getDataCount(String schema, String table, String updateColum, Date startDate, Date endDate) {
        return metaQueryClient.getDataCount(schema, table, updateColum, startDate, endDate);
    }
}
