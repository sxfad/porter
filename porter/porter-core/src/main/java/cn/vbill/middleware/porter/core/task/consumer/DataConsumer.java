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

import cn.vbill.middleware.porter.core.message.converter.EventConverter;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.task.consumer.Position;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 16:23
 */
public interface DataConsumer {
    /**
     * 消费源数据转换为suixinpay-datas数据模型转换器
     * @param converter
     */
    void setConverter(EventConverter converter);

    /**
     * 消费源客户端
     * @param c
     */
    void setClient(ConsumeClient c);

    /**
     * 消费源源数据查询客户端
     * @param metaQueryClient
     */
    void setMetaQueryClient(MetaQueryClient metaQueryClient);
    /**
     * 用于DataConsumer模块儿加载匹配
     * @param consumerName
     * @return
     */
    boolean isMatch(String consumerName);

    /**
     * 区分同一个任务的不同消费通道，用于分布式锁抢占、任务统计等
     * 获取消费器泳道ID
     * @return
     */
    String getSwimlaneId();

    /**
     * 资源控制
     * @throws InterruptedException
     */
    void shutdown() throws Exception;


    /**
     * 启动消费插件
     * @throws Exception
     */
    void startup() throws Exception;


    /**
     * 资源控制
     * @throws IOException
     */
    default boolean canStart() {
        return true;
    }

    /**
     * 从消费源获取数据
     * @return
     */
    List<MessageEvent> fetch() throws TaskStopTriggerException, InterruptedException;

    /**
     * 告警模块用于查询某个时间段数据变化数量
     * @param schema
     * @param table
     * @param updateColumnName
     * @param startDate
     * @param endDate
     * @return
     */
    int getDataCount(String schema, String table, String updateColumnName, Date startDate, Date endDate);

    /**
     * Excludes set方法
     *
     * @date 2018/8/8 下午5:44
     * @param: [excludes]
     * @return: void
     */
    void setExcludes(String excludes);

    /**
     * Includes set方法
     *
     * @date 2018/8/8 下午5:45
     * @param: [includes]
     * @return: void
     */
    void setIncludes(String includes);

    /**
     * 获取Excludes
     *
     * @date 2018/8/8 下午5:45
     * @param: []
     * @return: java.util.List<java.lang.String>
     */
    List<String> getExcludes();

    /**
     * 获取Includes
     *
     * @date 2018/8/8 下午5:45
     * @param: []
     * @return: java.util.List<java.lang.String>
     */
    List<String> getIncludes();

    /**
     * initializePosition
     *
     * @date 2018/8/8 下午5:45
     * @param: [taskId, swimlaneId, position]
     * @return: void
     */
    void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException;

    /**
     * commitPosition
     *
     * @date 2018/8/8 下午5:46
     * @param: [position]
     * @return: long
     */
    long commitPosition(Position position) throws TaskStopTriggerException;

    /**
     * 是否为AutoCommitPosition
     *
     * @date 2018/8/8 下午5:46
     * @param: []
     * @return: boolean
     */
    boolean isAutoCommitPosition();

    /**
     * 获取EventProcessor
     *
     * @date 2018/8/8 下午5:46
     * @param: []
     * @return: cn.vbill.middleware.porter.core.task.consumer.EventProcessor
     */
    EventProcessor getEventProcessor();

    /**
     * EventProcessor set方法
     *
     * @date 2018/8/8 下午5:47
     * @param: [eventProcessor]
     * @return: void
     */
    void setEventProcessor(EventProcessor eventProcessor);

    /**
     * 是否支持源数据源元数据查询:记录条数、schema
     * @return
     */
    default boolean supportMetaQuery() {
        return false;
    }

    /**
     * 获取ClientInfo
     *
     * @date 2018/8/8 下午5:47
     * @param: []
     * @return: java.lang.String
     */
    String getClientInfo();

    /**
     * 获取EmptyFetchNoticeSpan
     *
     * @date 2018/8/8 下午5:47
     * @param: []
     * @return: long
     */
    long getEmptyFetchNoticeSpan();

    /**
     * 获取EmptyFetchThreshold
     *
     * @date 2018/8/8 下午5:48
     * @param: []
     * @return: long
     */
    long getEmptyFetchThreshold();

    /**
     * EmptyFetchNoticeSpan set方法
     *
     * @date 2018/8/8 下午5:48
     * @param: [secondsValue]
     * @return: void
     */
    void setEmptyFetchNoticeSpan(long secondsValue);

    /**
     * EmptyFetchThreshold set方法
     *
     * @date 2018/8/8 下午5:48
     * @param: [secondsValue]
     * @return: void
     */
    void setEmptyFetchThreshold(long secondsValue);

    /**
     * 获取初始化消费点
     * @return
     */
    String getInitiatePosition();
    void setOffset(String offset);

    String getDefaultClientType();

    String getDefaultMetaClientType();
}
