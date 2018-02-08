/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.select;

import com.suixingpay.datas.common.util.ApplicationContextUtils;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.datacarrier.DataCarrier;
import com.suixingpay.datas.node.datacarrier.DataCarrierFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.List;

/**
 * 完成SQL事件从数据源的消费。为了提升消费能力，不做过多业务逻辑处理和数据模型转换
 * 获取事件消息，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:15
 */
public class SelectJob extends AbstractStageJob {
    private static final int PULL_BATCH_SIZE = 1000;
    //只需要保证select出来的数据不致使ExtractJob的所有消费线程饥饿即可
    private static final int BUFFER_SIZE = PULL_BATCH_SIZE*10;

    private final DataConsumer consumer;
    private final DataCarrier<MessageEvent> carrier;
    public SelectJob(TaskWork work) {
        super(work.getBasicThreadName());
        consumer = work.getDataConsumer();
        carrier = ApplicationContextUtils.INSTANCE.getBean(DataCarrierFactory.class).newDataCarrier(BUFFER_SIZE,PULL_BATCH_SIZE);
    }

    /**
     * 只有当DataCarrier数据消费完才能退出
     */
    @Override
    protected void doStop() {
        try {
            consumer.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doStart() throws IOException {
        consumer.startup();
    }

    @Override
    protected void loopLogic(){
        //只要队列有消息，持续读取
        List<MessageEvent> events = null;
        do {
            try {
                events = consumer.fetch();
                if (null != events) carrier.push(events);
            } catch (InterruptedException e) {
                LOGGER.error("fetch MessageEvent error!", e);
            }
        } while (null != events && ! events.isEmpty());
    }

    @Override
    public boolean canStart() {
        return null != consumer && consumer.canStart();
    }

    @Override
    public Pair<Long, List<MessageEvent>> output() {
        return carrier.greedyPullByOrder();
    }

    @Override
    public boolean stopWaiting() {
        return false;
    }

    @Override
    public boolean isPoolEmpty() {
        return carrier.size() == 0;
    }
}