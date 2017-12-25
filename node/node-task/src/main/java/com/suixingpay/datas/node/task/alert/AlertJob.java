package com.suixingpay.datas.node.task.alert;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.common.util.DefaultNamedThreadFactory;
import com.suixingpay.datas.node.core.task.AbstractStageJob;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class AlertJob extends AbstractStageJob{
    private final ScheduledExecutorService checkService;
    private final DataConnector source;
    private final DataConnector target;
    public AlertJob(DataConnector source, DataConnector target) {
        this.target = target;
        this.source = source;
        checkService = Executors.newScheduledThreadPool(2, new DefaultNamedThreadFactory(""));
    }
    @Override
    protected void doStop() {

    }

    @Override
    protected void doStart() {

    }
}
