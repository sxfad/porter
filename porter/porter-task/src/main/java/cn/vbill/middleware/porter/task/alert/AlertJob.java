/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.task.alert;

import cn.vbill.middleware.porter.core.NodeContext;
import cn.vbill.middleware.porter.common.statistics.NodeLog;
import cn.vbill.middleware.porter.core.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.loader.DataLoader;
import cn.vbill.middleware.porter.core.task.AbstractStageJob;
import cn.vbill.middleware.porter.task.alert.alerter.AlerterFactory;
import cn.vbill.middleware.porter.task.worker.TaskWork;

/**
 * 单线程执行，但存在多线程执行的可能性，前期单线程执行
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:20
 */
public class AlertJob extends AbstractStageJob {
    private final DataConsumer dataConsumer;
    private final DataLoader dataLoader;
    private final AlerterFactory alerterFactory;
    private final TaskWork work;
    public AlertJob(TaskWork work) {
        super(work.getBasicThreadName(), 1000 * 60 * 5L);
        this.dataConsumer = work.getDataConsumer();
        this.dataLoader = work.getDataLoader();
        alerterFactory = NodeContext.INSTANCE.getBean(AlerterFactory.class);
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
            alerterFactory.check(dataConsumer, dataLoader, work);
        } catch (Throwable e) {
            NodeLog.upload(NodeLog.LogType.TASK_LOG, work.getTaskId(), work.getDataConsumer().getSwimlaneId(), "db check error" + e.getMessage());
            LOGGER.error("[{}][{}]db check error!", work.getTaskId(), dataConsumer.getSwimlaneId(), e);
        }
    }

    @Override
    public <T> T output() throws Exception {
        throw new Exception("unsupported Method");
    }
}
