/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.jdbc;

import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.loader.SubmitStatObject;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcBatchLoader extends BaseJdbcLoader {

    @Override
    protected String getPluginName() {
        return LoaderPlugin.JDBC_BATCH.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        List<SubmitStatObject> affectRow = new ArrayList<>();
        for (List<ETLRow> rows : bucket.getBatchRows()) {
            if (rows.size() == 1) {
                ETLRow row = rows.get(0);
                //更新目标仓储
                int affect = loadSql(buildSql(row));

                //插入影响行数
                affectRow.add(new SubmitStatObject(row.getSchema(), row.getTable(), row.getOpType(), affect, row.getPosition(), row.getOpTime()));
            } else if (rows.size() > 1) { //仅支持单条记录生成一个sql的情况
                List<Pair<String, Object[]>> subList = new ArrayList<>();

                //生成sql
                for (int i = 0; i < rows.size(); i++) {
                    List<Pair<String, Object[]>> tmpSql = buildSql(rows.get(i));
                    subList.add(tmpSql.get(0));
                }

                //执行sql
                int[] results = batchLoadSql(subList);


                //处理状态变更
                for (int rindex = 0; rindex < rows.size(); rindex++) {
                    int affect = rindex < results.length ? results[rindex] : 0;
                    ETLRow row = rows.get(rindex);
                    affectRow.add(new SubmitStatObject(row.getSchema(), row.getTable(), row.getOpType(), affect, row.getPosition(), row.getOpTime()));
                }
            }
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }
}
