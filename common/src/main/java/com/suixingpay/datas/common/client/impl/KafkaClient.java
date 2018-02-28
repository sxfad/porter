/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.exception.ClientException;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class KafkaClient extends AbstractClient<KafkaConfig> implements ConsumeClient {

    private Consumer<String, String> consumer;
    private CountDownLatch canFetch = new CountDownLatch(1);
    private long perPullSize;

    public KafkaClient(KafkaConfig config) {
        super(config);
    }

    @Override
    protected void doStart() {
        KafkaConfig config = getConfig();
        perPullSize = config.getOncePollSize();
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, config.getGroup());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //单次消费数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getOncePollSize() + "");
        //从最开始的位置读取
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getFirstConsumeFrom());
        //设置offset默认每秒提交一次,不同于mysql binlog需要手动维护消费进度
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, config.getOncePollSize());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, config.isAutoCommit());
        Consumer<String, String> connector = new KafkaConsumer<String, String>(props);
        connector.subscribe(config.getTopics());
        this.consumer = connector;
        canFetch.countDown();
    }

    @Override
    public <F, O> List<F> fetch(FetchCallback<F, O> callback) {
        List<F> msgs = new ArrayList<>();
        if (isStarted()) {
            ConsumerRecords<String, String> results = consumer.poll(perPullSize);
            if (null != results && !results.isEmpty()) {
                Iterator<ConsumerRecord<String, String>> it = results.iterator();
                while (it.hasNext()) {
                    try {
                        ConsumerRecord<String, String> record = it.next();
                        F f = callback.accept(record);
                        if (null != f) {
                            msgs.add(f);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return msgs;
    }

    private boolean canSplit() {
        return getConfig().getTopics().size() > 1;
    }

    @Override
    public <T> List<T> split() throws ClientException {
        List<T> clients = new ArrayList<>();
        if (canSplit()) {
            for (String topic : getConfig().getTopics()) {
                KafkaConfig tmpConfig = getConfig();
                tmpConfig.setTopics(Arrays.asList(topic));
                T tmpClient = (T) AbstractClient.getClient(tmpConfig);
                clients.add(tmpClient);
            }
        } else {
            clients.add((T) this);
        }
        return clients;
    }

    @Override
    public String getSwimlaneId() {
        return StringUtils.join(getConfig().getTopics(), "_");
    }


    @Override
    protected void doShutdown() {
        if (null != consumer) {
            consumer.unsubscribe();
            consumer.close();
            consumer = null;
        }
        canFetch = new CountDownLatch(1);
    }

    @Override
    protected boolean isAlready() {
        try {
            canFetch.await();
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
}