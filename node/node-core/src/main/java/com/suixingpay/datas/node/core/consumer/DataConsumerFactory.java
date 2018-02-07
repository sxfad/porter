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
import com.suixingpay.datas.common.config.source.SourceConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.node.core.event.s.ConverterFactory;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.source.PublicSourceFactory;
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

    public List<DataConsumer> getConsumer(DataConsumerConfig config) throws ClientException, InstantiationException, IllegalAccessException {
        List<DataConsumer> consumers = new ArrayList<>();
        EventConverter converter = ConverterFactory.INSTANCE.getConverter(config.getConverter());


        Client tempClient = null;
        //获取源数据查询配置
        Config metaQueryConfig = Config.getConfig(config.getMetaSource());
        //如果是公共资源就从公共资源池获取
        if (metaQueryConfig instanceof SourceConfig && ((SourceConfig)metaQueryConfig).isPublic()) {
            tempClient = PublicSourceFactory.INSTANCE.getSource(((SourceConfig)metaQueryConfig).getSourceName());
        } else {
            tempClient = AbstractClient.getClient(metaQueryConfig);
        }

        MetaQueryClient metaQueryClient = null;
        if (tempClient instanceof MetaQueryClient) {
            metaQueryClient = (MetaQueryClient) tempClient;
        }

        if (null == metaQueryClient) throw new ClientException("MetaQueryClient初始化失败:" + config.getMetaSource());

        Config consumeConfig = Config.getConfig(config.getSource());

        ConsumeClient consumeClient = null;
        if (consumeConfig instanceof ConsumeClient) {
            consumeClient = (ConsumeClient) AbstractClient.getClient(consumeConfig);
        }
        if (null == consumeClient) throw new ClientException("ConsumeClient初始化失败:" + config.getSource());

        if (consumeClient.canSplit()) {
            List<ConsumeClient> consumeClients = consumeClient.split();
            //释放浪费创建的对象
            consumeClient = null;

            for (ConsumeClient c : consumeClients) {
                DataConsumer consumer = newConsumer(config.getConsumerName());
                consumer.setClient(c);
                consumer.setConverter(converter);
                consumer.setMetaQueryClient(metaQueryClient);
                consumers.add(consumer);
            }
        }

        return consumers;
    }

    public DataConsumer newConsumer(String consumerName) throws IllegalAccessException, InstantiationException {
        for (DataConsumer t : CONSUMER_TEMPLATE) {
            if (t.isMatch(consumerName)) {
                return t.getClass().newInstance();
            }
        }
        return null;
    }
}