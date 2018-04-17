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
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
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
        CONFIG.setTopics(Arrays.asList("SSP_debug"));
        CONFIG.setGroup("zkevin_0416211");
        CONFIG.setOncePollSize(2);
        CONFIG.setAutoCommit(false);
        CONFIG.setServers("172.16.154.5:9092,172.16.154.7:9092");
        KAFKA_CLIENT = new KafkaClient(CONFIG);
        KAFKA_CLIENT.start();
        KAFKA_CLIENT.initializePosition("1", "SSP_debug", "");
        //KAFKA_CLIENT.initializePosition("1", "SSP_debug", "{'topic':'SSP_debug','offset':14,'partition':0}");
    }



    @Test
    @Ignore
    public void fetch() {
        KAFKA_CLIENT.fetch(new ConsumeClient.FetchCallback<Object, Object>() {
            @Override
            public <F, O> F accept(O o) {
                ConsumerRecord<String, String> record = (ConsumerRecord<String, String>) o;
                System.out.println("----------" + record.value());
                try {
                    KAFKA_CLIENT.commitPosition(new KafkaClient.KafkaPosition(record.topic(), record.offset(), record.partition()));
                } catch (TaskStopTriggerException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    @Test
    @Ignore
    public void betchFetch() {
        fetch();
        fetch();
        fetch();
        fetch();
    }
}
