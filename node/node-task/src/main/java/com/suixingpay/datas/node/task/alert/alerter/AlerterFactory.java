/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:41
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.alert.alerter;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.util.compile.JavaFileCompiler;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.loader.DataLoader;
import com.suixingpay.datas.node.core.task.TableMapper;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
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
    private Alerter alerter;

    public AlerterFactory() {
        List<Alerter> alertList = SpringFactoriesLoader.loadFactories(Alerter.class, JavaFileCompiler.getInstance());
        if (null != alertList && alertList.size() == 1) {
            alerter = alertList.get(0);
        } else {
            throw new RuntimeException("AlerterFactory仅允许配置单个Alerter实现");
        }
    }

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
            } finally {
                service.shutdown();
            }
        } else {
            for (DTaskStat stat : stats) {
                alerter.check(dataConsumer, dataLoader, stat, getCheckMeta(work, stat.getSchema(), stat.getTable()), work.getReceivers());
            }
        }
    }


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
