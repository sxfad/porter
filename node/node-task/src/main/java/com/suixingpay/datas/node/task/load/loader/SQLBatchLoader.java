/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import com.suixingpay.datas.node.core.db.dialect.SqlTemplate;
import com.suixingpay.datas.node.core.event.ETLBucket;
import com.suixingpay.datas.node.core.event.ETLRow;
import com.suixingpay.datas.node.core.event.EventType;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 16:02
 */
public class SQLBatchLoader implements Loader {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SQLBatchLoader.class);
    private final DbDialectFactory dbFactory = DbDialectFactory.INSTANCE;
    //需要在之前对datasource进行二次封装
    @Override
    public void load(ETLBucket bucket, DTaskStat stat) {
        DbDialect dbDialect = dbFactory.getDbDialect(bucket.getDataSourceId());
        SqlTemplate template = dbDialect.getSqlTemplate();
        JdbcTemplate jdbcTemplate = dbDialect.getJdbcTemplate();
        for (ETLRow row : bucket.getRows()) {
            int affect = 0;
            String sql = "";
            try {
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
                    //全字段删除
                    sql = template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames);
                    if (jdbcTemplate.update(sql, allNewValues) < 1 && keyNames.length > 0) {
                        //主键删除
                        sql = template.getDeleteSql(row.getFinalSchema(), row.getFinalTable(), keyNames);
                        affect = jdbcTemplate.update(sql, keyNewValues);
                        if (affect < 1) {
                            LOGGER.debug("DELETE->ETLData:{},sequenceId:{}", JSON.toJSONString(row), bucket.getSequence());
                        }
                    }
                    stat.getDeleteRow().addAndGet(affect);
                } else if (row.getOpType() == EventType.INSERT) {
                    sql = template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames);
                    affect = jdbcTemplate.update(sql, allNewValues);
                    if (affect < 1) {
                        LOGGER.debug("INSERT->ETLData:{},sequenceId:{}", JSON.toJSONString(row), bucket.getSequence());
                    }
                    stat.getInsertRow().addAndGet(affect);
                } else if (row.getOpType() == EventType.UPDATE) {
                    //全字段更新
                    sql = template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames);
                    affect = jdbcTemplate.update(sql, ArrayUtils.addAll(allNewValues, allOldValues));
                    if (affect < 1 && keyNames.length > 0) {
                        //如果存在主键，根据主键更新
                        if (null != columnNames && columnNames.length > 0)  {
                            sql = template.getUpdateSql(row.getFinalSchema(), row.getFinalTable(), keyNames, columnNames);
                            affect = jdbcTemplate.update(sql, ArrayUtils.addAll(columnNewValues, keyOldValues));
                        }
                        if (affect < 1) {
                            //插入新值，老值冗余或是因为插入sql没有
                            sql = template.getInsertSql(row.getFinalSchema(), row.getFinalTable(), allColumnNames);
                            affect = jdbcTemplate.update(sql, allNewValues);
                        }
                    }
                    stat.getUpdateRow().addAndGet(affect);
                } else if (row.getOpType() == EventType.TRUNCATE) {
                    sql = template.getTruncateSql(row.getFinalSchema(), row.getFinalTable());
                    affect = jdbcTemplate.update(sql);
                    if (affect < 1) {
                        LOGGER.debug("TRUNCATE->ETLData:{},sequenceId:{}", JSON.toJSONString(row), bucket.getSequence());
                    }
                    stat.getDeleteRow().addAndGet(affect);
                }
                if (affect < 1) {
                    stat.getErrorRow().incrementAndGet();
                }
                //LOGGER.info("[{}]sql:{},affect:{}", row.getOpType(), sql, affect);
            } catch (Exception e) {
                LOGGER.error("{}->ETLData:{},sequenceId:{}",row.getOpType(), JSON.toJSONString(row), bucket.getSequence(), e);
            }
        }
     }
}
