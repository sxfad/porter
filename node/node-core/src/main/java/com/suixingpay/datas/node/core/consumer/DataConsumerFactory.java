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
import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.DataConsumerConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.exception.DataConsumerBuildException;
import com.suixingpay.datas.node.core.event.s.ConverterFactory;
import com.suixingpay.datas.node.core.event.s.EventConverter;
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
        List<DataConsumer> consumers = new ArrayList<>();
        EventConverter converter = ConverterFactory.INSTANCE.getConverter(config.getConverter());

        //获取源数据查询配置
        Client tempClient = AbstractClient.getClient(Config.getConfig(config.getMetaSource()));

        MetaQueryClient metaQueryClient = null;
        if (null != tempClient && tempClient instanceof MetaQueryClient) {
            metaQueryClient = (MetaQueryClient) tempClient;
        }

        if (null == metaQueryClient) throw new ClientException("MetaQueryClient初始化失败:" + config.getMetaSource());

        Client client = AbstractClient.getClient( Config.getConfig(config.getSource()));
        ConsumeClient consumeClient = null;
        if (null != client && client instanceof ConsumeClient) {
            consumeClient = (ConsumeClient)client;
        }
        if (null == consumeClient) throw new ClientException("ConsumeClient初始化失败:" + config.getSource());

        List<ConsumeClient> consumeClients = consumeClient.split();
        //释放浪费创建的对象
        for (ConsumeClient c : consumeClients) {
            DataConsumer consumer = newConsumer(config.getConsumerName());
            consumer.setClient(c);
            consumer.setConverter(converter);
            consumer.setMetaQueryClient(metaQueryClient);
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