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

package cn.vbill.middleware.porter.task.alert.alerter;

import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import cn.vbill.middleware.porter.core.loader.DataLoader;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import cn.vbill.middleware.porter.core.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.TableMapper;
import cn.vbill.middleware.porter.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:41
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 21:41
 */
@Component
@Scope("singleton")
public class AlerterFactory {
    //线程池线程数量
    private static final int CHECK_POOL_THREADS = 3;
    //当每次需要检查的表的数据量大于多少时，通过线程池执行
    private static final int CHECK_POOL_ACTIVE_TASK_VALUE = 5;
    private Alerter alerter;

    public AlerterFactory() {
        List<Alerter> alertList = SpringFactoriesLoader.loadFactories(Alerter.class, JavaFileCompiler.getInstance());
        if (null != alertList && alertList.size() == 1) {
            alerter = alertList.get(0);
        } else {
            throw new RuntimeException("AlerterFactory仅允许配置单个Alerter实现");
        }
    }

    /**
     * check
     *
     * @date 2018/8/9 下午2:02
     * @param: [dataConsumer, dataLoader, work]
     * @return: void
     */
    public void check(DataConsumer dataConsumer, DataLoader dataLoader, TaskWork work) throws InterruptedException {
        //任务统计列表
        List<DTaskStat> stats = work.getStats();
        if (stats.size() > CHECK_POOL_ACTIVE_TASK_VALUE) {
            //创建线程池
            ExecutorService service = Executors.newFixedThreadPool(CHECK_POOL_THREADS);
            //分配告警检查任务到线程池子
            for (DTaskStat stat : stats) {
                service.submit(() -> alerter.check(dataConsumer, dataLoader, stat, getCheckMeta(work, stat.getSchema(), stat.getTable()), work.getReceivers()));
            }
            //Initiates an orderly shutdown in which previously submitted  tasks are executed, but no new tasks will be accepted.
            service.shutdown();
            /* Blocks until all tasks have completed execution after a shutdown request
             * 基于任务停止释放资源的需要，最多等待5分钟
             */
            service.awaitTermination(5, TimeUnit.MINUTES);
        } else {
            for (DTaskStat stat : stats) {
                alerter.check(dataConsumer, dataLoader, stat, getCheckMeta(work, stat.getSchema(), stat.getTable()), work.getReceivers());
            }
        }
    }

    /**
     * 获取CheckMeta
     *
     * @date 2018/8/9 下午2:03
     * @param: [work, schema, table]
     * @return: org.apache.commons.lang3.tuple.Triple<java.lang.String[],java.lang.String[],java.lang.String[]>
     */
    private Triple<String[], String[], String[]> getCheckMeta(TaskWork work, String schema, String table) {
        TableMapper mapper = work.getTableMapper(schema, table);
        //初始化告警数据库查询信息
        String[] updateTime = null;
        String[] schemas = null;
        String[] tables = null;

        if (null != mapper && null != mapper.getUpdateDate() && mapper.getUpdateDate().length == 2) {
            updateTime = mapper.getUpdateDate();
        } else {
            updateTime = null;
        }
        if (null != mapper && null != mapper.getSchema() && mapper.getSchema().length == 2) {
            schemas = mapper.getSchema();
        } else {
            schemas = new String[]{schema, schema};
        }
        if (null != mapper && null != mapper.getTable() && mapper.getTable().length == 2) {
            tables = mapper.getTable();
        } else {
            tables = new String[]{table, table};
        }
        return new ImmutableTriple<>(schemas, tables, updateTime);
    }
}
