package com.suixingpay.datas.node.task.load;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.common.connector.DataConnector;
import com.suixingpay.datas.node.core.event.ETLBucket;
import com.suixingpay.datas.node.core.task.AbstractStageJob;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 完成SQL事件的最终执行，单线程执行,通过interrupt终止线程
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:19
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:19
 */
public class LoadJob extends AbstractStageJob{
    private final DataConnector target;

    public LoadJob(TaskWork work) {
        super(work.getBasicThreadName());
        this.target = work.getTarget();
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doStart() {

    }

    @Override
    protected void loopLogic() {

    }

    @Override
    public ETLBucket output() throws Exception {
        throw new Exception("unsupported Method");
    }
}
