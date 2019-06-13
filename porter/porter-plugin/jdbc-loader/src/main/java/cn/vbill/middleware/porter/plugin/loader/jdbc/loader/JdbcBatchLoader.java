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

package cn.vbill.middleware.porter.plugin.loader.jdbc.loader;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.statistics.DSubmitStatObject;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.plugin.loader.jdbc.JdbcLoaderConst;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcBatchLoader extends BaseJdbcLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcBatchLoader.class);

    @Override
    protected String getPluginName() {
        return JdbcLoaderConst.LOADER_PLUGIN_JDBC_BATCH.getCode();
    }

    @Override
    public Pair<Boolean, List<DSubmitStatObject>> doLoad(ETLBucket bucket) throws TaskStopTriggerException, InterruptedException {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        List<DSubmitStatObject> affectRow = new ArrayList<>();
        for (List<ETLRow> rows : bucket.getParallelRows()) {
            if (rows.size() == 1) {
                ETLRow row = rows.get(0);
                //更新目标仓储
                int affect = execSql(row);

                //插入影响行数
                affectRow.add(new DSubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                        affect, row.getPosition(), row.getOpTime()));
            } else if (rows.size() > 1) { //仅支持单条记录生成一个sql的情况
                //执行sql
                int[] results = execBatchSql(rows);
                //处理状态变更
                for (int rindex = 0; rindex < rows.size(); rindex++) {
                    int affect = rindex < results.length ? results[rindex] : 0;
                    ETLRow row = rows.get(rindex);
                    affectRow.add(new DSubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                            affect, row.getPosition(), row.getOpTime()));
                }
            }
        }
        if (null != bucket && null != bucket.getRows() && !bucket.getRows().isEmpty()) {
            printTimeTaken(bucket.getRows().get(0));
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }
    @Override
    public void sort(ETLBucket bucket) {
        int i = 0;
        while (i < bucket.getRows().size()) {
            System.out.println(i);
            List<ETLRow> groupOne = new ArrayList<>();
            ETLRow row = bucket.getRows().get(i);
            groupOne.add(row);
            i++;
            int j = i;
            while (j < bucket.getRows().size()) {
                ETLRow nextRow = bucket.getRows().get(j);
                //下个操作类型和该类型相同
                if (null != nextRow && nextRow.getFinalOpType() == row.getFinalOpType() && nextRow.getFinalSchema().equals(row.getFinalSchema())
                        && nextRow.getFinalTable().equals(row.getFinalTable())) {
                    groupOne.add(nextRow);
                    System.out.println(i + "-" + j);
                    j++;
                    i=j;
                } else {
                    break;
                }
            }
            bucket.getParallelRows().add(groupOne);
        }
    }
}
