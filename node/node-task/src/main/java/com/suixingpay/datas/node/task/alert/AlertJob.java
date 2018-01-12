/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.alert;

import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.common.util.ApplicationContextUtils;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.task.alert.alerter.AlerterFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

/**
 * 单线程执行，但存在多线程执行的可能性，前期单线程执行
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class AlertJob extends AbstractStageJob {
    private final DataSourceWrapper source;
    private final DataSourceWrapper target;
    private final AlerterFactory alerterFactory;
    private final TaskWork work;
    public AlertJob(TaskWork work) {
        super(work.getBasicThreadName(), 1000 * 60 * 5L );
        this.target = work.getTarget();
        this.source = work.getSource();
        alerterFactory = ApplicationContextUtils.INSTANCE.getBean(AlerterFactory.class);
        this.work = work;
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() {
        //10秒执行一次
        try {
            alerterFactory.check(source, target, work);
        } catch (Exception e) {
            LOGGER.error("[{}][{}]db check error!",work.getTaskId(), work.getTopic(), e);
        }
    }

    @Override
    public <T> T output() throws Exception {
        throw new Exception("unsupported Method");
    }
}
