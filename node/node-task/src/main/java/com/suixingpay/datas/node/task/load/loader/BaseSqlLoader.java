/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月09日 18:02
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.db.dialect.SqlTemplate;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.event.s.EventType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月09日 18:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月09日 18:02
 */
public class BaseSqlLoader {
    protected  final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /**
     * 用于一条记录有多个加载补偿策略。这种情况下
     * @param sqlList
     * @param jdbcTemplate
     * @return
     */
    protected int loadSql(List<Pair<String, Object[]>> sqlList, JdbcTemplate jdbcTemplate) {
        int affect = 0;
        for (Pair<String, Object[]> sqlOnce : sqlList) {
            affect = jdbcTemplate.update(sqlOnce.getLeft(), sqlOnce.getRight());
            if (affect > 0) break;
        }
        return affect;
    }

    /**
     * 正常来说批量执行的sql是完全一样的，但为了程序健壮性需要再次排序分组
     * @param sqlList
     * @param jdbcTemplate
     * @return
     */
    protected int[] batchLoadSql(List<Pair<String, Object[]>> sqlList, JdbcTemplate jdbcTemplate) {
        List<Pair<String, List<Object[]>>> reGroupList = new ArrayList<Pair<String, List<Object[]>>>();
        groupSql4Batch(reGroupList, sqlList, 0);
        int[] allAffects = new int[]{};
        for (Pair<String, List<Object[]>> batch : reGroupList) {
            int[] subResult = jdbcTemplate.batchUpdate(batch.getLeft(), batch.getRight());
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
        if (! currentGroup.isEmpty()) reGroupList.add(new ImmutablePair<>(currentSql, currentGroup));
        if (from < count) groupSql4Batch(reGroupList, sqlList, from);
    }


    /**
     * 生成Load SQL
     * @param row
     * @param template
     * @return
     */
    protected List<Pair<String, Object[]>> buildSql(ETLRow row, SqlTemplate template) {
        List<Pair<String, Object[]>> sqlList = new ArrayList<>();

        //提前构造SQL参数
        String[] keyNames = row.getSqlKeys().keySet().toArray(new String[]{});
        keyNames = null == keyNames ? new String[]{} : keyNames;
        String[] columnNames = row.getSqlColumns().keySet().toArray(new String[]{});
        columnNames = null == columnNames ? new String[]{} : columnNames;
        String[] allColumnNames = ArrayUtils.addAll(keyNames, columnNames);

        Object[] keyNewValues = row.getSqlKeys().values().stream().map(p -> p.getRight()).toArray();
        Object[] keyOldValues = row.getSqlKeys().values().stream().map(p -> p.getLeft()).toArray();
        Object[] columnNewValues = row.getSqlColumns().values().stream().map(p -> p.getRight()).toArray();
        Object[] columnOldValues = row.getSqlColumns().values().stream().map(p -> p.getLeft()).toArray();
        keyNewValues = null == keyNewValues ? new String[]{} : keyNewValues;
        keyOldValues = null == keyOldValues ? new String[]{} : keyOldValues;
        columnNewValues = null == columnNewValues ? new String[]{} : columnNewValues;
        columnOldValues = null == columnOldValues ? new String[]{} : columnOldValues;

        Object[] allNewValues = ArrayUtils.addAll(keyNewValues, columnNewValues);
        Object[] allOldValues = ArrayUtils.addAll(keyOldValues, columnOldValues);


        if (row.getOpType() == EventType.DELETE) {
            //主键删除
            if (keyNames.length > 0) {
                sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), keyNames), keyNewValues));
            }
            //全字段删除
            sqlList.add(new ImmutablePair<>(template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames), allNewValues));
        } else if (row.getOpType() == EventType.INSERT) {
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
            sqlList.add(new ImmutablePair<>(template.getTruncateSql(row.getFinalSchema(), row.getFinalTable()),new Object[]{}));
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
    protected void updateStat(Pair<Integer, ETLRow> result, DTaskStat stat){
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
           if (null != row.getOpTime()) stat.setLastLoadedTime(row.getOpTime());
           stat.setLastLoadedSystemTime(new Date());
           if (!StringUtils.isBlank(row.getIndex())) {
               stat.setProgress(row.getIndex());
           }
       }
    }
}
