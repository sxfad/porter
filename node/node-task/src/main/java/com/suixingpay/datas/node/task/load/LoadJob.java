/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.load;

import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.common.util.ApplicationContextUtils;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.core.task.StageType;
import com.suixingpay.datas.node.task.load.loader.LoaderFactory;
import com.suixingpay.datas.node.task.worker.TaskWork;

/**
 * 完成SQL事件的最终执行，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:19
 */
public class LoadJob extends AbstractStageJob{
    private final DataSourceWrapper target;
    private final TaskWork work;
    private final LoaderFactory loaderFactory;
    public LoadJob(TaskWork work) {
        super(work.getBasicThreadName());
        this.target = work.getTarget();
        this.work = work;
        loaderFactory = ApplicationContextUtils.INSTANCE.getBean(LoaderFactory.class);
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() {
        //只要队列有消息，持续读取
        ETLBucket bucket = null;
        do {
            try {
                bucket = work.waitEvent(StageType.TRANSFORM);
                if (null != bucket) loaderFactory.load(bucket , work.getStat());
            } catch (Exception e) {
                LOGGER.error("Load ETLRow error!", e);
            }
        } while (null != bucket && ! bucket.getRows().isEmpty());
    }

    @Override
    public ETLBucket output() throws Exception {
        throw new Exception("unsupported Method");
    }

    @Override
    public boolean isPrevPoolEmpty() {
        return work.isPoolEmpty(StageType.TRANSFORM);
    }
}
