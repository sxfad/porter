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

import cn.vbill.middleware.porter.common.task.exception.TaskDataException;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.task.statistics.DSubmitStatObject;
import cn.vbill.middleware.porter.plugin.connector.jdbc.JdbcConnectorConst;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.util.db.SqlUtils;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.core.task.loader.AbstractDataLoader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:57
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:57
 */
public abstract class BaseJdbcLoader extends AbstractDataLoader {
    protected SqlBuilder sqlBuilder;

    public BaseJdbcLoader() {
        if (null != getLoadClient()) sqlBuilder = new SqlBuilder(getLoadClient().getSqlTemplate(), isInsertOnUpdateError());
    }

    @Override
    public Pair<Boolean, List<DSubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException, InterruptedException {
        try {
            return doLoad(bucket);
        } catch (TaskStopTriggerException e) {
            if (e.getMessage().contains("interrupt") && e.getMessage().contains("CannotCreateTransactionException")) throw new InterruptedException(e.getMessage());
            throw e;
        }
    }

    public abstract Pair<Boolean, List<DSubmitStatObject>> doLoad(ETLBucket bucket) throws TaskStopTriggerException, InterruptedException;


    @Override
    public void mouldRow(ETLRow row) throws TaskDataException {
        if (null != row.getColumns()) {
            Map<String, Object> oldColumns = CustomETLRowField.getOldColumns(row);
            Map<String, Object> newColumns = CustomETLRowField.getNewColumns(row);
            for (ETLColumn c : row.getColumns()) {
                try {
                    Object newValue = SqlUtils.stringToSqlValue(c.getFinalValue(), c.getFinalType(), c.isRequired(), true);
                    Object oldValue = SqlUtils.stringToSqlValue(c.getFinalOldValue(), c.getFinalType(), c.isRequired(), true);
                    if (c.isKey()) {
                        CustomETLRowField.getSqlKeys(row).put(c.getFinalName(), new ImmutablePair<>(oldValue, newValue));
                    } else {
                        if (!c.isFinalAfterMissing()) newColumns.put(c.getFinalName(), newValue);
                        if (!c.isFinalBeforeMissing()) oldColumns.put(c.getFinalName(), oldValue);
                    }



                } catch (Exception e) {
                    StringBuilder log = new StringBuilder();
                    log.append("记录:").append(JSONObject.toJSONString(row))
                            .append(",点位:").append(row.getPosition().render())
                            .append(",字段名:").append(c.getFinalName()).append(",错误信息:").append(e.getMessage());
                    throw new TaskDataException(log.toString());
                }
            }
        }
    }


    /**
     * 自定义扩展字段
     */
    protected static class CustomETLRowField {

        private  static String SQL_KEYS_FIELD = "sqlKeys";
        private  static String SQL_OLD_COLUMNS_FIELD = "sqlOldColumns";
        private  static String SQL_NEW_COLUMNS_FIELD = "sqlNewColumns";

        protected static Map<String, Pair<Object, Object>> getSqlKeys(ETLRow row) {
            return (Map<String, Pair<Object, Object>>) row.getExtendsField().computeIfAbsent(SQL_KEYS_FIELD,
                k -> new LinkedHashMap<String, Pair<Object, Object>>());
        }

        protected static Map<String, Object> getOldColumns(ETLRow row) {
            return (Map<String, Object>) row.getExtendsField().computeIfAbsent(SQL_OLD_COLUMNS_FIELD,
                k -> new LinkedHashMap<String, Object>());
        }

        protected static Map<String, Object> getNewColumns(ETLRow row) {
            return (Map<String, Object>) row.getExtendsField().computeIfAbsent(SQL_NEW_COLUMNS_FIELD,
                k -> new LinkedHashMap<String, Object>());
        }
    }

    /**
     * 执行sql
     * @param row
     * @return
     */
    protected int execSql(ETLRow row) throws TaskStopTriggerException, InterruptedException {
        int affect = 0;
        int times = 0;
        try {
            List<Pair<String, Object[]>> sqlList = sqlBuilder.build(row);
            MessageAction action = row.getFinalOpType();
            for (Pair<String, Object[]> sqlOnce : sqlList) {
                times++;
                affect = getLoadClient().update(action.getValue(), sqlOnce.getLeft(), sqlOnce.getRight());
                if (affect > 0) break;
            }
        } catch (TaskStopTriggerException e) {
            //单条消息多种策略执行时，如果执行次数多于一次，不抛出异常
            if (times <= 1) throw e;
        }
        return affect;
    }

    /**
     * 以batch方式执行sql
     * @param rows
     * @return
     */
    protected int[] execBatchSql(List<ETLRow> rows) throws TaskStopTriggerException, InterruptedException {
        List<Pair<String, List<Object[]>>> reGroupList = new ArrayList<Pair<String, List<Object[]>>>();

        List<Pair<String, Object[]>> subList = new ArrayList<>();
        MessageAction action = rows.get(0).getFinalOpType();

        //生成sql
        for (int i = 0; i < rows.size(); i++) {
            List<Pair<String, Object[]>> tmpSql = sqlBuilder.build(rows.get(i));
            subList.add(tmpSql.get(0));
        }

        orderBatchRow(reGroupList, subList, 0);

        int[] allAffects = new int[]{};
        for (Pair<String, List<Object[]>> batch : reGroupList) {
            int[] subResult = getLoadClient().batchUpdate(action.getValue(), batch.getLeft(), batch.getRight());
            allAffects = ArrayUtils.addAll(allAffects, subResult);
        }
        return allAffects;
    }

    private void orderBatchRow(List<Pair<String, List<Object[]>>> reGroupList, List<Pair<String, Object[]>> sqlList, int from) {
        List<Object[]> currentGroup = new ArrayList<>();
        String currentSql = null;
        int count = sqlList.size();
        while (from < count) {
            Pair<String, Object[]> sql = sqlList.get(from);
            currentGroup.add(sql.getRight());
            currentSql = sql.getLeft().intern();
            from++;
            Pair<String, Object[]> nextSql = null;
            if (from < count) nextSql = sqlList.get(from);
            //如果下个sql和当前相同，继续添加当前批量sql参数
            if (null != nextSql && nextSql.getLeft().intern() == currentSql) {
                continue;
            } else {
                break;
            }
        }
        if (!currentGroup.isEmpty()) reGroupList.add(new ImmutablePair<>(currentSql, currentGroup));
        if (from < count) orderBatchRow(reGroupList, sqlList, from);
    }

    @Override
    public String getDefaultClientType() {
        return JdbcConnectorConst.LOAD_SOURCE_TYPE_NAME.getCode();
    }
}
