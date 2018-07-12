/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.client.impl;

import cn.vbill.middleware.porter.common.client.LoadClient;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.config.source.KafkaProduceConfig;
import cn.vbill.middleware.porter.common.db.meta.TableSchema;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class KafkaProduceClient extends AbstractClient<KafkaProduceConfig> implements LoadClient, MetaQueryClient {
    private volatile Producer<String, String> producer;
    private final String topic;
    private final boolean transaction;
    private final boolean oggJson;
    private final List<PartitionInfo> partitionInfoList = new ArrayList<>();
    private final Map<List<String>, List<String>> partitionKeyCache = new ConcurrentHashMap<>();
    private volatile CountDownLatch canProduce = new CountDownLatch(1);

    public KafkaProduceClient(KafkaProduceConfig config) {
        super(config);
        this.topic = config.getTopic();
        this.transaction = config.isTransaction();
        this.oggJson = config.isOggJson();
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
        if (transaction) producer.initTransactions();
        canProduce.countDown();
    }

    @Override
    protected void doShutdown() {
        if (null != producer) {
            producer.close();
            producer = null;
        }
        canProduce = new CountDownLatch(1);
    }

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

    public void send(String value, Integer partition, String key, boolean sync) throws TaskStopTriggerException {
        ProducerRecord<String, String> record = null;
        if (partition != null && partition > -1) {
            record = new ProducerRecord<>(topic, partition, key, value);
        } else {
            record = new ProducerRecord<>(topic, key, value);
        }
        sendTo(Arrays.asList(record), sync);
    }

    public void send(String value, boolean sync) throws TaskStopTriggerException {
        send(value, null, null, sync);
    }

    private void sendTo(List<ProducerRecord<String, String>> msgList, boolean sync) throws TaskStopTriggerException {
        try {
            canProduce.await();
            if (transaction) {
                producer.beginTransaction();
            }
            List<Future<RecordMetadata>> futures = new ArrayList<>();
            for (ProducerRecord<String, String> record : msgList) {
                if (sync) futures.add(producer.send(record));
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
        } catch (Throwable e) {
            throw new TaskStopTriggerException(e);
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


    public List<String> getPartitionKey(String schema, String table) {

        return partitionKeyCache.computeIfAbsent(Arrays.asList(schema, table), key -> {
            List<String>  keyNames = new ArrayList<>();
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
     * @return
     */
    private String getDefaultGroup() {
        return MachineUtils.IP_ADDRESS + "_" + MachineUtils.HOST_NAME + "_" + MachineUtils.CURRENT_JVM_PID;
    }


    public boolean renderOggJson() {
        return oggJson;
    }
}