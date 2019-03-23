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

package cn.vbill.middleware.porter.plugin.loader.kafka.client;

import cn.vbill.middleware.porter.common.client.*;
import cn.vbill.middleware.porter.common.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.plugin.loader.kafka.config.KafkaProduceConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class KafkaProduceClient extends AbstractClient<KafkaProduceConfig> implements PluginServiceClient, LoadClient, MetaQueryClient, StatisticClient {
    private volatile Producer<String, String> producer;
    private final String topic;
    private final boolean transaction;
    private final int retries;
    private final boolean oggJson;
    private final List<PartitionInfo> partitionInfoList = new ArrayList<>();
    private final Map<List<String>, List<String>> partitionKeyCache = new ConcurrentHashMap<>();
    private volatile CountDownLatch canProduce = new CountDownLatch(1);

    public KafkaProduceClient(KafkaProduceConfig config) {
        super(config);
        this.topic = config.getTopic();
        this.transaction = config.isTransaction();
        this.oggJson = config.isOggJson();
        this.retries = config.getRetries();
    }

    @Override
    protected void doStart() {
        KafkaProduceConfig config = getConfig();
        String group = StringUtils.isBlank(config.getGroup()) ? getDefaultGroup() : config.getGroup();
        final Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getServers());
        props.put(ProducerConfig.CLIENT_ID_CONFIG, group);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        if (transaction) {
            props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, group + "_" + System.nanoTime());
        }
        //props.put(ProducerConfig.ACKS_CONFIG, "1");
        //在重试次数大于0的情况下，严格保证produce顺序
        props.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, "1");
        producer = new KafkaProducer<>(props);
        partitionInfoList.addAll(producer.partitionsFor(topic));
        if (transaction) {
            producer.initTransactions();
        }
        canProduce.countDown();
    }

    @Override
    protected void doShutdown() {
        if (null != producer) {
            try {
                producer.close();
            } catch (Throwable e) {
                LOGGER.error("fail to close kafka producer,{}", topic, e);
            }
            producer = null;
        }
        canProduce = new CountDownLatch(1);
    }

    /**
     * send
     *
     * @param records
     * @param sync
     * @throws TaskStopTriggerException
     */
    public void send(List<Triple<String, String, Integer>> records, boolean sync) throws TaskStopTriggerException {
        List<ProducerRecord<String, String>> producerRecords = new ArrayList<>();
        records.forEach(t -> {
            ProducerRecord<String, String> record = null;
            if (t.getRight() != null && t.getRight() > -1) {
                record = new ProducerRecord<>(topic, t.getRight(), t.getLeft(), t.getMiddle());
            } else {
                record = new ProducerRecord<>(topic, t.getLeft(), t.getMiddle());
            }
            producerRecords.add(record);
        });
        sendTo(producerRecords, sync);
    }

    /**
     * send
     *
     * @param value
     * @param partition
     * @param key
     * @param sync
     * @throws TaskStopTriggerException
     */
    public void send(String value, Integer partition, String key, boolean sync) throws TaskStopTriggerException {
        ProducerRecord<String, String> record = null;
        if (partition != null && partition > -1) {
            record = new ProducerRecord<>(topic, partition, key, value);
        } else {
            record = new ProducerRecord<>(topic, key, value);
        }
        sendTo(Arrays.asList(record), sync);
    }

    /**
     * send
     *
     * @param value
     * @param sync
     * @throws TaskStopTriggerException
     */
    public void send(String value, boolean sync) throws TaskStopTriggerException {
        send(value, null, null, sync);
    }

    /**
     * send
     *
     * @param value
     * @param key
     * @throws TaskStopTriggerException
     */
    public void send(String value, String key, boolean sync) throws TaskStopTriggerException {
        send(value, null, key, sync);
    }

    private void sendTo(List<ProducerRecord<String, String>> msgList, boolean sync) throws TaskStopTriggerException {
        boolean sendResult = false;
        //做retries-1次尝试
        for (int i = 0; i < retries - 1; i++) {
            try {
                nativeSendTo(msgList, sync);
                sendResult = true;
                break;
            } catch (Throwable e) {
                LOGGER.error("fail to send message to kafka,times:{}", i, e);
                try {
                    Thread.sleep(500L);
                } catch (Throwable sleepException) {
                }
                //kafka异常，重置客户端
                if (e.getMessage().contains("org.apache.kafka.common.errors")) {
                    reconnection();
                }
            }
        }
        //做最后一次尝试，否则抛出异常
        if (!sendResult) {
            try {
                nativeSendTo(msgList, sync);
            } catch (Throwable e) {
                throw new TaskStopTriggerException(e);
            }
        }
    }

    /**
     * sendTo
     *
     * @param msgList
     * @param sync
     * @throws TaskStopTriggerException
     */
    private void nativeSendTo(List<ProducerRecord<String, String>> msgList, boolean sync) throws InterruptedException, ExecutionException {
        canProduce.await();
        if (transaction) {
            producer.beginTransaction();
        }
        List<Future<RecordMetadata>> futures = new ArrayList<>();
        for (ProducerRecord<String, String> record : msgList) {
            if (sync) {
                futures.add(producer.send(record));
            }
        }
        //等待结果
        for (Future<RecordMetadata> f : futures) {
            f.get();
        }
        if (transaction) {
            producer.commitTransaction();
        } else {
            producer.flush();
        }
    }

    @Override
    public TableSchema getTable(String schema, String tableName) {
        return null;
    }

    @Override
    public int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime) {
        return 0;
    }

    public List<PartitionInfo> getPartitionInfoList() {
        return Collections.unmodifiableList(partitionInfoList);
    }

    /**
     * sendTo
     *
     * @param schema
     * @param table
     * @return
     */
    public List<String> getPartitionKey(String schema, String table) {

        return partitionKeyCache.computeIfAbsent(Arrays.asList(schema, table), key -> {
            List<String> keyNames = new ArrayList<>();
            Map<String, String> partitionKeyMap = getConfig().getPartitionKey();
            if (null != partitionKeyMap && !partitionKeyMap.isEmpty()) {
                String keys = partitionKeyMap.getOrDefault(schema + "." + table, null);
                if (StringUtils.isNoneBlank(keys)) {
                    keyNames.addAll(Arrays.stream(keys.split(",")).collect(Collectors.toList()));
                }
            }
            return keyNames;
        });
    }

    /**
     * 当没有配置group时，做默认配置
     *
     * @return
     */
    private String getDefaultGroup() {
        return MachineUtils.IP_ADDRESS + "_" + MachineUtils.HOST_NAME + "_" + MachineUtils.CURRENT_JVM_PID;
    }

    /**
     * sendTo
     *
     * @return
     */
    public boolean renderOggJson() {
        return oggJson;
    }

    private synchronized void reconnection() {
        doShutdown();
        doStart();
    }

    @Override
    public void uploadStatistic(String target, String key, String data) {
        try {
            send(data, key, true);
        } catch (Throwable e) {
            LOGGER.warn("上传统计信息失败,忽略异常", e);
        }
    }


}