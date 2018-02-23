/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.util.Arrays;
import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 17:01
 */
public class KafkaClientTest {
    private static KafkaClient kafkaClient;
    private static KafkaConfig config;

    @BeforeClass
    public static void initSource() {
        config = new KafkaConfig();
        config.setTopics(Arrays.asList("zkw.t_user_debug_001"));
        config.setGroup(UUID.randomUUID().toString());
        config.setOncePollSize(1);
        config.setServers("172.16.135.30:9092,172.16.135.30:9093");
        kafkaClient = new KafkaClient(config);
    }



    @Test
    public void fetch() {
        kafkaClient.fetch(new ConsumeClient.FetchCallback<Object, Object>() {
            @Override
            public <F, O> F accept(O o) throws ParseException {
                System.out.println(JSON.toJSONString(o));
                return null;
            }
        });
    }
}
