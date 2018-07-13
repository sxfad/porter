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
