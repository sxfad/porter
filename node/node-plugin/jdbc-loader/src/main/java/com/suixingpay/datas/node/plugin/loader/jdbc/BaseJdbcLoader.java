/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:57
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.jdbc;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.client.impl.JDBCClient;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.db.SqlTemplate;
import com.suixingpay.datas.common.db.SqlUtils;
import com.suixingpay.datas.common.exception.TaskDataException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.etl.ETLColumn;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.core.loader.AbstractDataLoader;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Date;

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
    protected int loadSql(List<Pair<String, Object[]>> sqlList) throws TaskStopTriggerException {
        JDBCClient client = getLoadClient();
        int affect = 0;
        for (Pair<String, Object[]> sqlOnce : sqlList) {
            affect = client.update(sqlOnce.getLeft(), sqlOnce.getRight());
            if (affect > 0) break;
        }
        return affect;
    }

    /**
     * 正常来说批量执行的sql是完全一样的，但为了程序健壮性需要再次排序分组
     * @param sqlList
     * @return
     */
    protected int[] batchLoadSql(List<Pair<String, Object[]>> sqlList) throws TaskStopTriggerException {
        JDBCClient client = getLoadClient();
        List<Pair<String, List<Object[]>>> reGroupList = new ArrayList<Pair<String, List<Object[]>>>();
        groupSql4Batch(reGroupList, sqlList, 0);
        int[] allAffects = new int[]{};
        for (Pair<String, List<Object[]>> batch : reGroupList) {
            int[] subResult = client.batchUpdate(batch.getLeft(), batch.getRight());
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
        Map<String, Pair<Object, Object>> sqlColumns  = CustomETLRowField.getSqlColumns(row);

        JDBCClient client = getLoadClient();
        SqlTemplate template = client.getSqlTemplate();



        //主键名
        String[] keyNames = sqlKeys.keySet().toArray(new String[]{});
        //非主键名
        String[] columnNames = sqlColumns.keySet().toArray(new String[]{});
        //所有字段名
        String[] allColumnNames = ArrayUtils.addAll(keyNames, columnNames);

        //主键新值
        Object[] keyNewValues = sqlKeys.values().stream().map(p -> p.getRight()).toArray();
        //主键旧值
        Object[] keyOldValues = sqlKeys.values().stream().map(p -> p.getLeft()).toArray();

        //非主键新值
        Object[] columnNewValues = sqlColumns.values().stream().map(p -> p.getRight()).toArray();
        //非主键旧值
        Object[] columnOldValues = sqlColumns.values().stream().map(p -> p.getLeft()).toArray();

        //所有字段新值
        Object[] allNewValues = ArrayUtils.addAll(keyNewValues, columnNewValues);
        //所有字段旧值
        Object[] allOldValues = ArrayUtils.addAll(keyOldValues, columnOldValues);



        if (row.getOpType() == EventType.DELETE) {
            //主键删除
            if (keyNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), keyNames), keyNewValues));
            }
            //全字段删除
            sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getOpType() == EventType.INSERT) {
            //插入sql
            sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getOpType() == EventType.UPDATE) {
            //如果存在主键，根据主键更新
            if (keyNames.length > 0 && null != columnNames && columnNames.length > 0)  {
                sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), keyNames, columnNames),
                        ArrayUtils.addAll(columnNewValues, keyOldValues)));
            }

            //全字段更新
            sqlList.add(new ImmutablePair<>(template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames),
                    ArrayUtils.addAll(allNewValues, allOldValues)));

            //插入
            sqlList.add(new ImmutablePair<>(template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames),
                    allNewValues));
        } else if (row.getOpType() == EventType.TRUNCATE) {
            sqlList.add(new ImmutablePair<>(template.getTruncateSql(row.getFinalSchema(), row.getFinalTable()), new Object[]{}));
        }
        return sqlList;
    }


    /**
     * 更新任务状态
     *  For a prepared statement batch, it is not possible to know the number of rows affected in the database
     *  by each individual statement in the batch.Therefore, all array elements have a value of -2.
     *  According to the JDBC 2.0 specification, a value of -2 indicates that the operation was successful
     *  but the number of rows affected is unknown.
     * @param result
     * @param stat
     */
    protected void updateStat(Pair<Integer, ETLRow> result, DTaskStat stat) {
        //虽然每个状态值的变更都有stat对象锁，但在最外层加对象锁避免了多次请求的问题（锁可重入），同时保证状态各字段变更一致性
        synchronized (stat) {
            ETLRow row = result.getRight();
            switch (result.getRight().getOpType().getIndex()) {
                case EventType.DELETE_INDEX:
                    if (result.getLeft() > 0 || result.getLeft() == -2) {
                        stat.incrementDeleteRow();
                    } else {
                        stat.incrementErrorDeleteRow();
                    }
                    break;
                case EventType.UPDATE_INDEX:
                    if (result.getLeft() > 0 || result.getLeft() == -2) {
                        stat.incrementUpdateRow();
                    } else {
                        stat.incrementErrorUpdateRow();
                    }
                    break;
                case EventType.INSERT_INDEX:
                    if (result.getLeft() > 0 || result.getLeft() == -2) {
                        stat.incrementInsertRow();
                    } else {
                        stat.incrementErrorInsertRow();
                    }
                    break;
                case EventType.TRUNCATE_INDEX:
                    if (result.getLeft() > 0 || result.getLeft() == -2) {
                        stat.incrementDeleteRow();
                    } else {
                        stat.incrementErrorDeleteRow();
                    }
                    break;
            }

            //更新最后执行消息事件的产生时间，用于计算从消息产生到加载如路时间、计算数据同步检查时间
            if (null != row.getOpTime()) stat.setLastLoadedDataTime(row.getOpTime());
            stat.setLastLoadedSystemTime(new Date());
            if (!StringUtils.isBlank(row.getPosition())) {
                stat.setProgress(row.getPosition());
            }
        }
    }

    @Override
    public void mouldRow(ETLRow row) throws TaskDataException {
        if (null != row.getColumns()) {
            for (ETLColumn c : row.getColumns()) {
                try {
                    Object newValue = SqlUtils.stringToSqlValue(c.getFinalValue(), c.getFinalType(), c.isRequired(), true);
                    Object oldValue = SqlUtils.stringToSqlValue(c.getFinalOldValue(), c.getFinalType(), c.isRequired(), true);
                    if (c.isKey()) {
                        CustomETLRowField.getSqlKeys(row).put(c.getFinalName(), new ImmutablePair<>(oldValue, newValue));
                    } else {
                        CustomETLRowField.getSqlColumns(row).put(c.getFinalName(), new ImmutablePair<>(oldValue, newValue));
                    }
                } catch (Exception e) {
                    StringBuilder log = new StringBuilder();
                    log.append("记录:").append(JSONObject.toJSONString(row))
                            .append(",点位:").append(row.getPosition())
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
        private  static String SQL_COLUMNS_FIELD = "sqlColumns";

        protected static Map<String, Pair<Object, Object>> getSqlKeys(ETLRow row) {
            return (Map<String, Pair<Object, Object>>) row.getExtendsField().computeIfAbsent(SQL_KEYS_FIELD,
                k -> new LinkedHashMap<String, Pair<Object, Object>>());
        }

        protected static Map<String, Pair<Object, Object>> getSqlColumns(ETLRow row) {
            return (Map<String, Pair<Object, Object>>) row.getExtendsField().computeIfAbsent(SQL_COLUMNS_FIELD,
                k -> new LinkedHashMap<String, Pair<Object, Object>>());
        }
    }
}
