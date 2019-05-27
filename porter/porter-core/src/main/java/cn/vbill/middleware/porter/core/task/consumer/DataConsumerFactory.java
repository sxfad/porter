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

package cn.vbill.middleware.porter.core.task.consumer;

import cn.vbill.middleware.porter.core.message.converter.ConverterFactory;
import cn.vbill.middleware.porter.core.message.converter.EventConverter;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.task.config.DataConsumerConfig;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.task.exception.DataConsumerBuildException;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

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
    private final List<String> consumerTemplate = SpringFactoriesLoader.loadFactoryNames(DataConsumer.class, JavaFileCompiler.getInstance());
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(DataConsumerFactory.class);

    /**
     * 获取Consumer
     *
     * @date 2018/8/8 下午5:52
     * @param: [config]
     * @return: java.util.List<cn.vbill.middleware.porter.core.task.consumer.DataConsumer>
     */
    public List<DataConsumer> getConsumer(DataConsumerConfig config) throws ClientException, ConfigParseException, DataConsumerBuildException {
        //创建consumer对象
        DataConsumer tmpConsumer = newConsumer(config.getConsumerName());
        //消息转换器
        EventConverter converter = ConverterFactory.INSTANCE.getConverter(StringUtils.isNotBlank(config.getConverter())
                ? config.getConverter() : tmpConsumer.getDefaultEventConverter());

        List<DataConsumer> consumers = new ArrayList<>();

        if (null == converter) {
            throw new ClientException("消息转换器EventConverter未配置或不存在:" + config.getConverter());
        }

        //获取源数据查询配置
        MetaQueryClient metaQueryClient = null;
        if (null != config.getMetaSource() && !config.getMetaSource().isEmpty()) {
            Client client = AbstractClient.getClient(SourceConfig.getConfig(config.getMetaSource(), tmpConsumer.getDefaultMetaClientType()));
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
        SourceConfig configSet = SourceConfig.getConfig(config.getSource(), tmpConsumer.getDefaultClientType());
        if (null == configSet) throw new ConfigParseException(config.getSource() + "不能识别的数据源");
        List<SourceConfig> configs = configSet.swamlanes();
        for (SourceConfig sourceConfig : configs) {
            Client tempClient = AbstractClient.getClient(sourceConfig);
            if (null == tempClient || !(tempClient instanceof ConsumeClient)) {
                throw new ConfigParseException(config.getSource() + "不能识别的消费端链接信息");
            }
            ConsumeClient consumeClient = (ConsumeClient) tempClient;

            //创建consumer对象
            DataConsumer consumer = newConsumer(tmpConsumer);
            consumer.setClient(consumeClient);
            consumer.setConverter(converter);

            if (null == metaQueryClient && tempClient instanceof MetaQueryClient) {
                consumer.setMetaQueryClient((MetaQueryClient) tempClient);
            }

            consumer.setExcludes(config.getExcludes());
            consumer.setIncludes(config.getIncludes());
            consumer.setEventProcessor(processor);
            consumer.setOffset(config.getOffset());

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
     * @return: cn.vbill.middleware.porter.core.task.consumer.DataConsumer
     */
    public DataConsumer newConsumer(String consumerName) throws DataConsumerBuildException {
        for (String t : consumerTemplate) {
            try {
                Class<DataConsumer> tmp = (Class<DataConsumer>) ClassUtils.forName(t, DataConsumer.class.getClassLoader());
                DataConsumer tmpInstance = tmp.newInstance();
                if (tmpInstance.isMatch(consumerName)) {
                    return tmpInstance;
                }
            } catch (Throwable e) {
                logger.warn("{}不匹配{}", t, consumerName, e);
            }
        }
        throw new DataConsumerBuildException();
    }

    public DataConsumer newConsumer(DataConsumer clazz) throws DataConsumerBuildException {
        try {
            return clazz.getClass().newInstance();
        } catch (Throwable e) {
            throw new DataConsumerBuildException();
        }
    }
}