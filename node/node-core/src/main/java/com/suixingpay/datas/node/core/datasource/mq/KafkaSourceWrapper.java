/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.datasource.mq;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.datasource.AbstractSourceWrapper;
import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.MQDataSourceWrapper;
import com.suixingpay.datas.common.datasource.meta.KafkaDriverMeta;
import com.suixingpay.datas.node.core.event.EventFetcher;
import com.suixingpay.datas.node.core.event.EventHeader;
import com.suixingpay.datas.node.core.event.EventType;
import com.suixingpay.datas.node.core.event.MessageEvent;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 09:52
 */
public class KafkaSourceWrapper extends AbstractSourceWrapper implements EventFetcher, MQDataSourceWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSourceWrapper.class);
    private static final DateFormat OP_TS_F = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static final DateFormat C_TS_F = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
    private final String FIRST_CONSUME_FROM = "earliest";

    private String topic;
    private final String group;
    //ip:port;ip:port
    private final String servers;
    private final Long  oncePollSize;
    private final Long  oncePollTimeout;
    private Consumer<String, String> consumer;
    private CountDownLatch canFetch;

    public KafkaSourceWrapper(DataDriver driver) {
        super(driver);
        KafkaDriverMeta meta = (KafkaDriverMeta)driver.getType().getMeta();
        servers = driver.getUrl();
        group = driver.getExtendAttr().getOrDefault(meta.GROUP,"");
        topic = driver.getExtendAttr().getOrDefault(meta.TOPIC,"");
        String oncePoolSizeStr= driver.getExtendAttr().getOrDefault(meta.ONCE_POLL_SIZE,"500");
        oncePollSize = Long.parseLong(oncePoolSizeStr);
        String oncePoolTimeoutStr= driver.getExtendAttr().getOrDefault(meta.POLL_TIME_OUT,"10000");
        oncePollTimeout = Long.parseLong(oncePoolTimeoutStr);
        canFetch = new CountDownLatch(1);
    }

    @Override
    public  List<MessageEvent> fetch() {
        try {
            canFetch.await();
        } catch (InterruptedException e) {
            LOGGER.error("等待kafka状态可用异常", e);
        }
        List<MessageEvent> msgs = new ArrayList<MessageEvent>();
        ConsumerRecords<String, String> results = consumer.poll(oncePollTimeout);
        if (null == results ||results.isEmpty()) return msgs;
        Iterator<ConsumerRecord<String, String>> it = results.iterator();
        while (it.hasNext()) {
            try {
                ConsumerRecord<String, String> record = it.next();
                JSONObject obj = JSON.parseObject(record.value());
                EventType eventType = EventType.type(obj.getString("op_type"));
                //不能解析的事件跳过
                if(null == eventType ||  eventType == EventType.UNKNOWN) continue;

                EventHeader eventHeader = new EventHeader();
                eventHeader.setKey(record.key());
                eventHeader.setOffset(record.offset());
                eventHeader.setPartition(record.partition());
                eventHeader.setTopic(record.topic());

                //body
                MessageEvent event = new MessageEvent();
                String schemaAndTable = obj.getString("table");
                String[] stTmp = null != schemaAndTable ? schemaAndTable.split("\\.") : null;
                if (null != stTmp && stTmp.length == 2) {
                    event.setSchema(stTmp[0]);
                    event.setTable(stTmp[1]);
                }
                event.setOpType(eventType);
                String poTS = obj.getString("op_ts");
                event.setOpTs(OP_TS_F.parse(poTS.substring(0,poTS.length() - 3)));
                String currentTS = obj.getString("current_ts");
                event.setCurrentTs(C_TS_F.parse(currentTS.substring(0, currentTS.length() - 3)));
                JSONArray pkeys = obj.containsKey("primary_keys") ? obj.getJSONArray("primary_keys") : null;
                if (null != pkeys) event.setPrimaryKeys(pkeys.toJavaList(String.class));
                event.setBefore(obj.getObject("before",Map.class));
                event.setAfter(obj.getObject("after",Map.class));
                event.setHead(eventHeader);

                msgs.add(event);
            } catch (Exception e) {
            }
        }
        return msgs;
    }

    @Override
    protected void doDestroy() {
        if (null != consumer) {
            consumer.unsubscribe();
            consumer.close();
            consumer = null;
        }
        canFetch = new CountDownLatch(1);
    }

    @Override
    protected void doCreate() {
        final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, group);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        //单次消费数量
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, oncePollSize + "");
        //从最开始的位置读取
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, FIRST_CONSUME_FROM);
        //设置offset默认每秒提交一次,不同于mysql binlog需要手动维护消费进度
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true );
        Consumer<String, String> connector = new KafkaConsumer<String, String>(props);
        connector.subscribe(Arrays.asList(topic));
        this.consumer = connector;
        canFetch.countDown();
    }

    public void  setTopic(String topic) {
        this.topic =topic;
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
