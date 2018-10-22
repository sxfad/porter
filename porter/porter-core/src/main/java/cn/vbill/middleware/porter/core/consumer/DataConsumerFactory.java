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

package cn.vbill.middleware.porter.core.consumer;

import cn.vbill.middleware.porter.core.event.s.ConverterFactory;
import cn.vbill.middleware.porter.core.event.s.EventConverter;
import cn.vbill.middleware.porter.core.event.s.EventProcessor;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.DataConsumerBuildException;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /**
     * INSTANCE
     */
    INSTANCE();
    private final List<DataConsumer> consumerTemplate = SpringFactoriesLoader.loadFactories(DataConsumer.class, JavaFileCompiler.getInstance());
    private static final Logger LOGGER = LoggerFactory.getLogger(DataConsumerFactory.class);

    /**
     * 获取Consumer
     *
     * @date 2018/8/8 下午5:52
     * @param: [config]
     * @return: java.util.List<cn.vbill.middleware.porter.core.consumer.DataConsumer>
     */
    public List<DataConsumer> getConsumer(DataConsumerConfig config) throws ClientException, ConfigParseException, DataConsumerBuildException {
        //消息转换器
        EventConverter converter = ConverterFactory.INSTANCE.getConverter(config.getConverter());

        List<DataConsumer> consumers = new ArrayList<>();


        //获取源数据查询配置
        MetaQueryClient metaQueryClient = null;
        if (null != config.getMetaSource() && !config.getMetaSource().isEmpty()) {
            Client client = AbstractClient.getClient(SourceConfig.getConfig(config.getMetaSource()));
            if (client instanceof MetaQueryClient) {
                metaQueryClient = (MetaQueryClient) client;
            } else {
                throw new ClientException("MetaQueryClient初始化失败:" + config.getMetaSource());
            }

        }

        //自定义消费数据处理器
        EventProcessor processor = null;
        if (null != config.getEventProcessor()) {
            try {
                processor = JavaFileCompiler.getInstance().newJavaObject(config.getEventProcessor(), EventProcessor.class);
            } catch (Exception e) {
                throw new ConfigParseException("EventProcessor转换java对象失败:" + e.getMessage());
            }
        }

        //消费数据获取来源
        List<SourceConfig> configs = SourceConfig.getConfig(config.getSource()).swamlanes();
        for (SourceConfig sourceConfig : configs) {
            Client tempClient = AbstractClient.getClient(sourceConfig);
            if (null == tempClient || !(tempClient instanceof ConsumeClient)) {
                throw new ClientException("ConsumeClient初始化失败:" + config.getSource());
            }
            ConsumeClient consumeClient = (ConsumeClient) tempClient;

            //创建consumer对象
            DataConsumer consumer = newConsumer(config.getConsumerName());
            consumer.setClient(consumeClient);
            consumer.setConverter(converter);
            consumer.setMetaQueryClient(metaQueryClient);
            consumer.setExcludes(config.getExcludes());
            consumer.setIncludes(config.getIncludes());
            consumer.setEventProcessor(processor);

            /**
             * 空查询告警参数
             */
            consumer.setEmptyFetchNoticeSpan(config.getEmptyFetchNoticeSpan());
            consumer.setEmptyFetchThreshold(config.getEmptyFetchThreshold());
            consumers.add(consumer);
        }
        return consumers;
    }

    /**
     * newConsumer
     *
     * @date 2018/8/8 下午5:53
     * @param: [consumerName]
     * @return: cn.vbill.middleware.porter.core.consumer.DataConsumer
     */
    public DataConsumer newConsumer(String consumerName) throws DataConsumerBuildException {
        for (DataConsumer t : consumerTemplate) {
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