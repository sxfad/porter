/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:09
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.consumer;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.consumer.ConsumeClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.config.DataConsumerConfig;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.exception.DataConsumerBuildException;
import com.suixingpay.datas.common.util.compile.JavaFileCompiler;
import com.suixingpay.datas.node.core.event.s.ConverterFactory;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.EventProcessor;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 10:09
 */
public enum DataConsumerFactory {
    INSTANCE();
    private final List<DataConsumer> CONSUMER_TEMPLATE = SpringFactoriesLoader.loadFactories(DataConsumer.class, null);

    public List<DataConsumer> getConsumer(DataConsumerConfig config) throws ClientException, ConfigParseException, DataConsumerBuildException {
        //消息转换器
        EventConverter converter = ConverterFactory.INSTANCE.getConverter(config.getConverter());

        List<DataConsumer> consumers = new ArrayList<>();


        //获取源数据查询配置
        Client tempMetaQueryClient = AbstractClient.getClient(SourceConfig.getConfig(config.getMetaSource()));

        MetaQueryClient metaQueryClient = null;
        if (null != tempMetaQueryClient && tempMetaQueryClient instanceof MetaQueryClient) {
            metaQueryClient = (MetaQueryClient) tempMetaQueryClient;
        }
        if (null == metaQueryClient) throw new ClientException("MetaQueryClient初始化失败:" + config.getMetaSource());


        //消费数据获取来源
        Client tempConsumeclient = AbstractClient.getClient(SourceConfig.getConfig(config.getSource()));

        ConsumeClient consumeClient = null;
        if (null != tempConsumeclient && tempConsumeclient instanceof ConsumeClient) {
            consumeClient = (ConsumeClient) tempConsumeclient;
        }
        if (null == consumeClient) throw new ClientException("ConsumeClient初始化失败:" + config.getSource());


        //自定义消费数据处理器
        EventProcessor processor = null;
        if (null != config.getEventProcessor()) {
            try {
                processor = JavaFileCompiler.getInstance().newJavaObject(config.getEventProcessor(), EventProcessor.class);
            } catch (Exception e) {
                throw new ConfigParseException("EventProcessor转换java对象失败:" + e.getMessage());
            }
        }

        List<ConsumeClient> consumeClients = consumeClient.splitSwimlanes();
        //释放浪费创建的对象
        for (ConsumeClient c : consumeClients) {
            DataConsumer consumer = newConsumer(config.getConsumerName());
            consumer.setClient(c);
            consumer.setConverter(converter);
            consumer.setMetaQueryClient(metaQueryClient);
            consumer.setExcludes(config.getExcludes());
            consumer.setIncludes(config.getIncludes());
            consumer.setEventProcessor(processor);
            consumers.add(consumer);
        }
        return consumers;
    }

    public DataConsumer newConsumer(String consumerName) throws DataConsumerBuildException {
        for (DataConsumer t : CONSUMER_TEMPLATE) {
            if (t.isMatch(consumerName)) {
                try {
                    return t.getClass().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DataConsumerBuildException(e.getMessage());
                }
            }
        }
        return null;
    }
}