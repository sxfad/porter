/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
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
    protected void doStart() throws TaskStopTriggerException {
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
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommitPosition());
        Consumer<String, String> connector = new KafkaConsumer<>(props);
        this.consumer = connector;
        if (isAutoCommitPosition()) {
            connector.subscribe(config.getTopics());
            canFetch.countDown();
        }
    }

    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {
        try {
            if (!isAutoCommitPosition()) {
                if (!StringUtils.isBlank(position)) {
                    KafkaPosition kafkaPosition = KafkaPosition.getPosition(position);
                    TopicPartition tp = new TopicPartition(kafkaPosition.topic, kafkaPosition.partition);
                    synchronized (consumer) {
                        consumer.assign(Arrays.asList(tp));
                        //AUTO_OFFSET_RESET_CONFIG值等于none时，如果position大于topic有效offset时会抛出OffsetOutOfRangeException
                        consumer.seek(tp, kafkaPosition.offset + 1);
                    }
                } else {
                    //默认消费分区0,该消费组上次
                    consumer.assign(Arrays.asList(new TopicPartition(swimlaneId, 0)));
                }
                canFetch.countDown();
            }
        } catch (Throwable e) {
            throw new TaskStopTriggerException(e);
        }
    }

    @Override
    public <F, O> List<F> fetch(FetchCallback<F, O> callback) {
        List<F> msgs = new ArrayList<>();
        if (isStarted()) {
            ConsumerRecords<String, String> results = null;
            synchronized (consumer) {
                results = consumer.poll(perPullSize);
            }
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
    public <T> List<T> splitSwimlanes() throws ClientException, ConfigParseException {
        List<T> clients = new ArrayList<>();
        if (canSplit()) {
            for (String topic : getConfig().getTopics()) {
                KafkaConfig tmpConfig = SourceConfig.getConfig(getConfig().getProperties());
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
    public boolean isAutoCommitPosition() {
        return getConfig().isAutoCommit();
    }

    @Override
    public String getSwimlaneId() {
        return !getConfig().getTopics().isEmpty() ? getConfig().getTopics().get(0) : StringUtils.EMPTY;
    }

    @Override
    public  void commitPosition(String position) throws TaskStopTriggerException {
        //如果提交方式为手动提交
        if (!isAutoCommitPosition()) {
            try {
                KafkaPosition kafkaPosition = KafkaPosition.getPosition(position);
                synchronized (consumer) {
                    consumer.commitSync(new HashMap<TopicPartition, OffsetAndMetadata>() {
                        {
                            put(new TopicPartition(kafkaPosition.topic, kafkaPosition.partition), new OffsetAndMetadata(kafkaPosition.offset));
                        }
                    });
                }
            } catch (Throwable e) {
                throw new TaskStopTriggerException(e);
            }
        }
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

    @AllArgsConstructor
    private static class KafkaPosition {
        private final String topic;
        private final long offset;
        private final int partition;

        private static KafkaPosition getPosition(String position) throws TaskStopTriggerException {
            try {
                JSONObject object = JSONObject.parseObject(position);
                String topic = object.getString("topic");
                long offset = object.getLongValue("offset");
                int partition = object.getIntValue("partition");
                return new KafkaPosition(topic, offset, partition);
            } catch (Throwable throwable) {
                throw new TaskStopTriggerException(throwable);
            }
        }
    }
}
