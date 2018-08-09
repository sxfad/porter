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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:41
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 21:41
 */
@Component
@Scope("singleton")
public class AlerterFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlerterFactory.class);

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
    public void check(DataConsumer dataConsumer, DataLoader dataLoader, TaskWork work) {
        //任务统计列表
        List<DTaskStat> stats = work.getStats();
        if (stats.size() > 5) {
            //创建屏障
            CyclicBarrier barrier = new CyclicBarrier(stats.size() + 1);

            //创建线程池
            int threadSize = 3;
            ExecutorService service = Executors.newFixedThreadPool(threadSize);
            //分配告警检查任务到线程池子
            for (DTaskStat stat : stats) {
                service.submit(new Runnable() {
                    @Override
                    public void run() {
                        //执行任务
                        try {
                            alerter.check(dataConsumer, dataLoader, stat, getCheckMeta(work, stat.getSchema(), stat.getTable()), work.getReceivers());
                        } finally {
                            try {
                                barrier.await();
                            } catch (Exception e) {
                                e.printStackTrace();
                                LOGGER.error("%s", e);
                            }
                        }
                    }
                });
            }
            //关闭线程池
            try {
                barrier.await();
            } catch (Exception e) {
                e.printStackTrace();
                LOGGER.error("%s", e);
            } finally {
                service.shutdown();
            }
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
            schemas = new String[] {schema, schema};
        }
        if (null != mapper && null != mapper.getTable() && mapper.getTable().length == 2) {
            tables = mapper.getTable();
        } else {
            tables = new String[] {table, table};
        }
        return new ImmutableTriple<>(schemas, tables, updateTime);
    }
}
