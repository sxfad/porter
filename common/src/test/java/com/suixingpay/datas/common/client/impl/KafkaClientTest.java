/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.suixingpay.datas.common.consumer.ConsumeClient;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 17:01
 */
public class KafkaClientTest {
    private static KafkaClient KAFKA_CLIENT;
    private static KafkaConfig CONFIG;

    @BeforeClass
    public static void initSource() throws Exception {
        CONFIG = new KafkaConfig();
        CONFIG.setTopics(Arrays.asList("kafkaLoader"));
        CONFIG.setGroup("zkevin_041621111");
        CONFIG.setOncePollSize(1000);
        CONFIG.setAutoCommit(false);
        CONFIG.setServers("172.16.154.5:9092,172.16.154.7:9092");
        KAFKA_CLIENT = new KafkaClient(CONFIG);
        KAFKA_CLIENT.start();
        KAFKA_CLIENT.initializePosition("1", "kafkaLoader", "{'topic':'kafkaLoader','offset':0,'partition':1}");
    }



    @Test
    @Ignore
    public void fetch() {
        KAFKA_CLIENT.fetch(new ConsumeClient.FetchCallback<Object, Object>() {
            @Override
            public <F, O> F accept(O o) {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                System.out.println(record.partition() + "----------" + record.key() + "----------" + record.value());
                return null;
            }
        });
    }
}
