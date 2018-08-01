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

import cn.vbill.middleware.porter.core.event.s.EventConverter;
import cn.vbill.middleware.porter.core.event.s.EventProcessor;
import cn.vbill.middleware.porter.core.event.s.MessageEvent;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.consumer.Position;

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

    void setExcludes(String excludes);

    void setIncludes(String includes);

    List<String> getExcludes();

    List<String> getIncludes();

    void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException;

    long commitPosition(Position position) throws TaskStopTriggerException;

    boolean isAutoCommitPosition();

    EventProcessor getEventProcessor();

    void setEventProcessor(EventProcessor eventProcessor);

    /**
     * 是否支持源数据源元数据查询:记录条数、schema
     * @return
     */
    default boolean supportMetaQuery() {
        return false;
    }


    String getClientInfo();

    long getEmptyFetchNoticeSpan();
    long getEmptyFetchThreshold();

    void setEmptyFetchNoticeSpan(long secondsValue);
    void setEmptyFetchThreshold(long secondsValue);
}
