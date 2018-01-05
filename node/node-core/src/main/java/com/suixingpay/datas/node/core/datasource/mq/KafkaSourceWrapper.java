/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.datasource.mq;

import com.suixingpay.datas.common.datasource.AbstractSourceWrapper;
import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.MQDataSourceWrapper;
import com.suixingpay.datas.common.datasource.meta.KafkaDriverMeta;
import com.suixingpay.datas.node.core.event.s.*;
import com.suixingpay.datas.node.core.event.s.converter.ConvertNotMatchException;
import com.suixingpay.datas.node.core.event.s.converter.OggConverter;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 09:52
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 09:52
 */
public class KafkaSourceWrapper extends AbstractSourceWrapper implements EventFetcher, MQDataSourceWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSourceWrapper.class);
    private final String FIRST_CONSUME_FROM = "earliest";

    private String topic;
    private final String group;
    //ip:port;ip:port
    private final String servers;
    private final Long  oncePollSize;
    private final Long  oncePollTimeout;
    private Consumer<String, String> consumer;
    private CountDownLatch canFetch;

    //MessageEvent转换器
    private EventConverter converter;
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
        //配置转换器
        String converterName = driver.getExtendAttr().getOrDefault(meta.CONVERTER, "");
        converter = ConverterFactory.INSTANCE.getConverter(converterName);
    }

    @Override
    public EventConverter getConverter() {
        return converter;
    }

    @Override
    public  List<MessageEvent> fetch() {
        if (null == converter) {
            String msg = "消息转换器不可用，消息消费暂停!";
            LOGGER.error("topic:{},group:{}" + msg, topic, group, new Exception(msg));
            return null;
        }
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
                MessageEvent event = getConverter().convert(record);
                if (null !=event) msgs.add(event);
            } catch (ConvertNotMatchException notMatchException) {
                LOGGER.error("消息转换器不匹配", notMatchException);
                converter = null;
            } catch (Exception e) {
                e.printStackTrace();
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
