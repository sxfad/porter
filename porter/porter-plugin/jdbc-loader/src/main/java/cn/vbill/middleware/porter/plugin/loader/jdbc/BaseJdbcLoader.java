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

package cn.vbill.middleware.porter.plugin.loader.jdbc;

import cn.vbill.middleware.porter.common.exception.TaskDataException;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.loader.SubmitStatObject;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.client.impl.JDBCClient;
import cn.vbill.middleware.porter.common.db.SqlTemplate;
import cn.vbill.middleware.porter.common.db.SqlUtils;
import cn.vbill.middleware.porter.core.event.etl.ETLColumn;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import cn.vbill.middleware.porter.core.event.s.EventType;
import cn.vbill.middleware.porter.core.loader.AbstractDataLoader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:57
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:57
 */
public abstract class BaseJdbcLoader extends AbstractDataLoader {
    /**
     * 用于一条记录有多个加载补偿策略。这种情况下
     * @param sqlList
     * @return
     */
    protected int loadSql(List<Pair<String, Object[]>> sqlList, EventType eventType) throws TaskStopTriggerException {
        int affect = 0;
        int times = 0;
        try {
            JDBCClient client = getLoadClient();
            for (Pair<String, Object[]> sqlOnce : sqlList) {
                times++;
                affect = client.update(eventType.getValue(), sqlOnce.getLeft(), sqlOnce.getRight());
                if (affect > 0) break;
            }
        } catch (TaskStopTriggerException e) {
            //单条消息多种策略执行时，如果执行次数多于一次，不抛出异常
            if (times <= 1) throw e;
        }
        return affect;
    }

    /**
     * 正常来说批量执行的sql是完全一样的，但为了程序健壮性需要再次排序分组
     * @param sqlList
     * @return
     */
    protected int[] batchLoadSql(List<Pair<String, Object[]>> sqlList, EventType eventType) throws TaskStopTriggerException {
        JDBCClient client = getLoadClient();
        List<Pair<String, List<Object[]>>> reGroupList = new ArrayList<Pair<String, List<Object[]>>>();
        groupSql4Batch(reGroupList, sqlList, 0);
        int[] allAffects = new int[]{};
        for (Pair<String, List<Object[]>> batch : reGroupList) {
            int[] subResult = client.batchUpdate(eventType.getValue(), batch.getLeft(), batch.getRight());
            allAffects = ArrayUtils.addAll(allAffects, subResult);
        }
        return allAffects;
    }

    private void groupSql4Batch(List<Pair<String, List<Object[]>>> reGroupList, List<Pair<String, Object[]>> sqlList, int from) {
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
        if (from < count) groupSql4Batch(reGroupList, sqlList, from);
    }


    /**
     * 生成Load SQL
     * @param row
     * @return
     */
    protected List<Pair<String, Object[]>> buildSql(ETLRow row) {
        //build sql
        List<Pair<String, Object[]>> sqlList = new ArrayList<>();

        //获取自定义字段
        Map<String, Pair<Object, Object>> sqlKeys = CustomETLRowField.getSqlKeys(row);
        //数组组建类型
        Class strArrayComponent = new String[0].getClass().getComponentType();

        Map<String, Object> oldColumns = CustomETLRowField.getOldColumns(row);
        Map<String, Object> newColumns = CustomETLRowField.getNewColumns(row);

        JDBCClient client = getLoadClient();
        SqlTemplate template = client.getSqlTemplate();

        //主键名
        String[] keyNames = sqlKeys.keySet().toArray(new String[]{});
        //主键新值
        Object[] keyNewValues = sqlKeys.values().stream().map(p -> p.getRight()).toArray();
        //主键旧值
        Object[] keyOldValues = sqlKeys.values().stream().map(p -> p.getLeft()).toArray();

        if (row.getFinalOpType() == EventType.DELETE) {
            //主键删除
            if (keyNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), keyNames), keyNewValues));
            }

            //全字段删除
            //1.数组条件
            String[] allColumnNames = addArray(strArrayComponent, keyNames, newColumns.keySet().toArray(new String[0]));
            //所有字段新值
            Object[] allNewValues = addArray(keyNewValues, newColumns.values().toArray());
            //拼接sql
            sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getFinalOpType() == EventType.INSERT) {
            //1.数组条件
            String[] allColumnNames = addArray(strArrayComponent, keyNames, newColumns.keySet().toArray(new String[0]));
            //所有字段新值
            Object[] allNewValues = addArray(keyNewValues, newColumns.values().toArray());
            //插入sql
            sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getFinalOpType() == EventType.UPDATE) {
            String[] columnNames = newColumns.keySet().toArray(new String[0]);
            Object[] columnValues = newColumns.values().toArray();
            //存在主键，主键值没变，根据主键更新
            if (!row.isKeyChangedOnUpdate() && keyNames.length > 0 && null != columnNames && columnNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), keyNames, columnNames),
                        addArray(columnValues, keyOldValues)));
            }
            //全字段更新
            Object[] oldColumnValues = oldColumns.values().toArray();
            String[] oldColumnNames = oldColumns.keySet().toArray(new String[0]);
            sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(),
                    addArray(strArrayComponent, keyNames, oldColumnNames), addArray(strArrayComponent, keyNames, columnNames)),
                    addArray(keyNewValues, columnValues, keyOldValues, oldColumnValues)));

            //更新转插入
            if (isInsertOnUpdateError()) {
                sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(),
                        addArray(strArrayComponent, keyNames, columnNames)), addArray(keyNewValues, columnValues)));
            }
        } else if (row.getFinalOpType() == EventType.TRUNCATE) {
            sqlList.add(new ImmutablePair<>(template.getTruncateSql(row.getFinalSchema(), row.getFinalTable()), new Object[]{}));
        }
        return sqlList;
    }

    private <T> T[] addArray(T[]... array) {
        return addArray(Object.class, array);
    }

    private <T> T[] addArray(Class componentType, T[]... array) {
        List<T> newArray = new ArrayList<>();
        Arrays.stream(array).forEach(a -> {
            if (null != a && a.length > 0) {
                newArray.addAll(Arrays.stream(a).collect(Collectors.toList()));
            }
        });
        return newArray.toArray((T[]) Array.newInstance(componentType, 0));
    }
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

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException, InterruptedException {
        try {
            return doLoad(bucket);
        } catch (TaskStopTriggerException e) {
            if (e.getMessage().contains("interrupt") && e.getMessage().contains("CannotCreateTransactionException")) throw new InterruptedException(e.getMessage());
            throw e;
        }
    }

    public abstract Pair<Boolean, List<SubmitStatObject>> doLoad(ETLBucket bucket) throws TaskStopTriggerException;
}
