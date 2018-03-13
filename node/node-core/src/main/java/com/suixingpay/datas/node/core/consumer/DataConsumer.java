/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:23
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.consumer;

import com.suixingpay.datas.common.client.ConsumeClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.EventProcessor;
import com.suixingpay.datas.node.core.event.s.MessageEvent;

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
    List<MessageEvent> fetch();

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

    void commitPosition(String position) throws TaskStopTriggerException;

    boolean isAutoCommitPosition();

    EventProcessor getEventProcessor();

    void setEventProcessor(EventProcessor eventProcessor);
}