package com.suixingpay.datas.node.core.task;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 阶段性工作
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:04
 */
public abstract class AbstractStageJob implements StageJob {
    private AtomicBoolean stat = new AtomicBoolean(false);

    protected abstract void doStop();

    protected abstract void doStart();

    @Override
    public void start() {
        if (canStart() && stat.compareAndSet(false, true)) {
            try {
                doStart();
            } catch (Exception e) {
                doStop();
            }
        } else {

        }
    }



    @Override
    public void stop() {
        if (stat.compareAndSet(true, false)) {
            try {
                doStop();
            } catch (Exception e) {
            }
        } else {

        }
    }
}
