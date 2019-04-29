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
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.core.task.statistics.DSubmitStatObject;
import cn.vbill.middleware.porter.plugin.loader.jdbc.JdbcLoaderConst;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * jdbc多线程并发Load
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:38
 */
public class JdbcMultiThreadLoader extends BaseJdbcLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcMultiThreadLoader.class);
    @Override
    protected String getPluginName() {
        return JdbcLoaderConst.LOADER_PLUGIN_JDBC_MULTI_THREAD.name();
    }

    @Override
    public Pair<Boolean, List<DSubmitStatObject>> doLoad(ETLBucket bucket) throws InterruptedException, TaskStopTriggerException {
        List<DSubmitStatObject> affectRow = new ArrayList<>();
        ForkJoinPool pool = new ForkJoinPool();
        List<ForkJoinTask<List<DSubmitStatObject>>> futures = new ArrayList<>(bucket.getParallelRows().size());
        bucket.getParallelRows().stream().forEach(rows -> {
            futures.add(pool.submit(() -> {
                int[] result = execBatchSql(rows);
                List<DSubmitStatObject> subResult = new ArrayList<>();
                for (int i = 0; i < rows.size(); i++) {
                    ETLRow row = rows.get(i);
                    int affect = i < result.length ? result[i] : 0;
                    subResult.add(new DSubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                            affect, row.getPosition(), row.getOpTime()));
                }
                return subResult;
            }));
        });
        for (ForkJoinTask<List<DSubmitStatObject>> future : futures) {
            try {
                List<DSubmitStatObject> subResult = future.get();
                if (future.isCompletedAbnormally()) throw new TaskStopTriggerException(future.getException());
                affectRow.addAll(subResult);
            } catch (ExecutionException e) {
                throw new TaskStopTriggerException(e);
            }
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }

    @Override
    public void sort(ETLBucket bucket) {
        //表并行
        Map<List<String>, List<ETLRow>> tables = new HashMap<>();
        bucket.getRows().stream().forEach(row -> {
            tables.compute(Arrays.asList(row.getFinalSchema(), row.getFinalTable()), (k, v) -> {
                if (null == v) v = new ArrayList<>();
                v.add(row);
                return v;
            });
        });

        //表内数据并行
        tables.forEach((k, v) -> {
            groupRows(bucket, v);
        });
    }


    private void groupRows(ETLBucket bucket, List<ETLRow> rows) {
        int rowSize = rows.size();
        int span = rowSize % 2 == 0 ? rowSize / 2 : rowSize / 2 + 1;
        for (int i = 0; i < rowSize; i += span) {
            int endIndex = (span + i) > rowSize ? rowSize : span + i;
            List<ETLRow> currentRow = rows.subList(i, endIndex);
            if (i == 0) {
                List<ETLRow> nextRow = rows.subList(endIndex, rowSize);
                List<String> nextKeys = nextRow.stream().flatMap(r -> rowKeyList(r).stream()).distinct().collect(Collectors.toList());
                if (currentRow.stream().filter(r -> rowKeyList(r).stream().filter(k -> nextKeys.contains(k)).count() > 0).count() > 0) {
                    bucket.getParallelRows().add(rows);
                    break;
                }
            }

            if (currentRow.size() < 2 || currentRow.stream().filter(r -> rowKeyList(r).stream().filter(k ->
                    currentRow.stream().filter(z -> z != r).flatMap(z -> rowKeyList(z).stream()).distinct()
                            .collect(Collectors.toList()).contains(k)
            ).count() > 0).count() > 0) {
                bucket.getParallelRows().add(currentRow);
            } else {
                groupRows(bucket, currentRow);
            }
        }
    }

    private List<String> rowKeyList(ETLRow row) {
        List<String> keys = new ArrayList<>(2);
        keys.add(row.getColumns().stream().filter(c -> c.isKey()).sorted(Comparator.comparing(ETLColumn::getFinalName)).
                map(c -> c.getFinalName() + "_" + (null != c.getFinalValue() ? c.getFinalValue() : "")).reduce((p, n) -> p + "@" + n).get());
        if (row.getFinalOpType() != MessageAction.INSERT) {
            keys.add(row.getColumns().stream().filter(c -> c.isKey()).sorted(Comparator.comparing(ETLColumn::getFinalName)).
                    map(c -> c.getFinalName() + "_" + (null != c.getFinalOldValue() ? c.getFinalOldValue() : "")).reduce((p, n) -> p + "@" + n).get());
        }
        return keys;
    }
}
