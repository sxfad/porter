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
import com.suixingpay.datas.common.consumer.ConsumeClient;
import com.suixingpay.datas.common.config.source.KafkaConfig;
import com.suixingpay.datas.common.consumer.Position;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.common.util.MachineUtils;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

    private final Map<String, AtomicReference<KafkaPosition>> lazyCommitOffsetMap = new ConcurrentHashMap<>();
    private long pollTimeOut;
    public KafkaClient(KafkaConfig config) {
        super(config);
    }

    @Override
    protected void doStart() {
        KafkaConfig config = getConfig();
        pollTimeOut = config.getPollTimeOut();
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, StringUtils.isBlank(config.getGroup()) ? getDefaultGroup() : config.getGroup());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //单次消费数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, config.getOncePollSize() + "");
        //从最开始的位置读取
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, config.getFirstConsumeFrom());
        //设置offset默认每秒提交一次,不同于mysql binlog需要手动维护消费进度
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, config.getOnceCommitInterval());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommitPosition());
        Consumer<String, String> connector = new KafkaConsumer<>(props);
        this.consumer = connector;
        if (isAutoCommitPosition()) {
            connector.subscribe(config.getTopics());
            canFetch.countDown();
        }
    }

    /**
     * 当没有配置group时，做默认配置
     * @return
     */
    private String getDefaultGroup() {
        return MachineUtils.IP_ADDRESS + "_" + MachineUtils.HOST_NAME;
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
                //提交kafka消费进度
                commitLazyPosition();
                results = consumer.poll(pollTimeOut);
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

    @Override
    public boolean isAutoCommitPosition() {
        return getConfig().isAutoCommit();
    }

    @Override
    public String getSwimlaneId() {
        return getConfig().getSwimlaneId();
    }

    @Override
    public  void commitPosition(Position position) throws TaskStopTriggerException {
        //如果提交方式为手动提交
        if (!isAutoCommitPosition()) {
            try {
                KafkaPosition kafkaPosition = (KafkaPosition) position;
                //由于kafka不是线程安全的缘故，commitPosition与fetch属于不同的线程，有对象锁的机制。
                //为保证消费效率，取消对象锁，这里只做offset提交请求，在fetch时先做commitPosition再fetch数据
                AtomicReference reference = lazyCommitOffsetMap.computeIfAbsent(kafkaPosition.getPositionKey(), key -> new AtomicReference<>());
                synchronized (reference) {
                    reference.set(kafkaPosition);
                }
                /**
                synchronized (consumer) {
                    consumer.commitSync(new HashMap<TopicPartition, OffsetAndMetadata>() {
                        {
                            put(new TopicPartition(kafkaPosition.topic, kafkaPosition.partition), new OffsetAndMetadata(kafkaPosition.offset));
                        }
                    });
                }
                 **/
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

    /**
     * kafka位点信息
     */
    public static class KafkaPosition extends Position {
        @Getter private final String topic;
        @Getter private final long offset;
        @Getter private final int partition;
        private final boolean checksum;
        public KafkaPosition(String topic, long offset, int partition) {
            this.topic = topic;
            this.offset = offset;
            this.partition = partition;
            this.checksum = !StringUtils.isBlank(topic) && offset > -1 && partition > -1;
        }
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

        public String getPositionKey() {
            return topic + "_" + partition;
        }

        @Override
        public boolean checksum() {
            return checksum;
        }
    }

    private void commitLazyPosition() {
        //每次查询数据前都要提交
        lazyCommitOffsetMap.values().stream().filter(v -> null != v.get()).forEach(v -> {
            KafkaPosition position = null;
            synchronized (v) {
                position = v.get();
                v.set(null);
            }
            if (null != position) {
                consumer.commitSync(Collections.singletonMap(new TopicPartition(position.topic, position.partition),
                        new OffsetAndMetadata(position.offset)));
            }
        });
    }
}