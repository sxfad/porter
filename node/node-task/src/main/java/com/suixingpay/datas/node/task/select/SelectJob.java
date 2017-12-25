package com.suixingpay.datas.node.task.select;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.event.EventFetcher;
import com.suixingpay.datas.node.core.event.MessageEvent;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:15
 */
public class SelectJob extends AbstractStageJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelectJob.class);
    private final DataConnector consumer;
    private final Thread  fetcherJob;
    private final DefaultNamedThreadFactory factory;
    public SelectJob(DataConnector consumerSource, DefaultNamedThreadFactory factory) {
        super();
        this.factory = factory;
        if (consumerSource instanceof  EventFetcher) {
            consumer = consumerSource;
            fetcherJob = factory.newThread(new FetcherJob((EventFetcher) consumerSource),"SelectJob");
        } else {
            consumer = null;
            fetcherJob = null;
        }
    }

    @Override
    protected void doStop() {
        fetcherJob.interrupt();
        consumer.disconnect();
    }

    @Override
    protected void doStart() {
        fetcherJob.start();
        consumer.connect();
    }

    @Override
    public boolean canStart() {
        return null != consumer;
    }

    private  class FetcherJob implements Runnable {
        private final EventFetcher fetcher;
        public FetcherJob(EventFetcher fetcher) {
            this.fetcher = fetcher;
        }

        @Override
        public void run() {
            //如果线程没有中断信号，持续执行
            while (!Thread.currentThread().isInterrupted()) {
                //只要队列有消息，持续读取
                List<MessageEvent> events = null;
                do {
                    try {
                        events = fetcher.fetch();
                    } catch (Exception e){
                        LOGGER.error("fetch MessageEvent error!", e);
                    }
                } while (null != events && ! events.isEmpty());
                //当前时间段MessageEvent队列消息取完，线程沉睡10秒
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {//如果线程有中断信号，退出线程
                    break;
                }
            }
        }
    }
}
