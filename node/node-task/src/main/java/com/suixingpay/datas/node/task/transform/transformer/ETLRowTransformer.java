/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.transform.transformer;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.utils.SqlUtils;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLColumn;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 13:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 13:38
 */
public class ETLRowTransformer implements Transformer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLRowTransformer.class);

    @Override
    public int order() {
        return 0;
    }

    @Override
    public void transform(ETLBucket bucket, TaskWork work, DbDialect targetDialect) {
        LOGGER.debug("start tranforming bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        for (ETLRow row : bucket.getRows()) {
            LOGGER.debug("try tranform row:{},{}", row.getIndex(), JSON.toJSONString(row));
            TableMapper tableMapper = work.getTableMapper(row.getSchema(), row.getTable());
            mappingRowData(tableMapper, row);
            Table table = findTable(targetDialect, row.getFinalSchema(), row.getFinalTable());
            if (null != table) remedyColumns(table, row);
            createSQLPrepare(row);
            LOGGER.debug("after tranform row:{},{}", row.getIndex(), JSON.toJSONString(row));
        }
    }

    private void mappingRowData(TableMapper mapper, ETLRow row) {
        if (null != mapper && mapper.isCustom()) {
            //替换schema和表名
            if (null != mapper.getSchema() && mapper.getSchema().length == 2) row.setFinalSchema(mapper.getSchema()[1]);
            if (null != mapper.getTable() && mapper.getTable().length == 2) row.setFinalTable(mapper.getTable()[1]);
            if (null != row.getColumns() && null != mapper.getColumns()) {
                for (ETLColumn c : row.getColumns()) {
                    //替换字段名
                    c.setFinalName(mapper.getColumns().getOrDefault(c.getName(), c.getName()));
                }
            }
        }
    }

    private void remedyColumns(Table table, ETLRow row) {
        List<ETLColumn> removeables = new ArrayList<>();
        //正向查找
        for (ETLColumn c : row.getColumns()) {
            Column column = table.findColumn(c.getFinalName());
            if (null != column) {
                c.setFinalType(column.getTypeCode());
                c.setKey(column.isPrimaryKey());
                c.setRequired(column.isRequired());
                //如果是更新并且原主键不存在
                if (row.getOpType() == EventType.UPDATE && column.isRequired() && StringUtils.isBlank(c.getOldValue())) {
                    c.setOldValue(c.getNewValue());
                }
            } else {
                removeables.add(c);
            }
        }
        //反向查找
        List<String> inColumnNames = row.getColumns().stream().map(p  -> p.getFinalName()).collect(Collectors.toList());
        //如果目标库表中有新增必填的字段需要补充上去,并且是插入操作时
        Arrays.stream(table.getColumns()).filter(p -> ! inColumnNames.contains(p.getName()) && p.isRequired() && null == p.getDefaultValue()
                && row.getOpType() == EventType.INSERT)
                .forEach(p -> row.getColumns().add(new ETLColumn(p.getName(), p.getDefaultValue() ,p.getDefaultValue(), p.getDefaultValue(), p.isPrimaryKey(), p.isRequired(), p.getTypeCode())));

        row.getColumns().removeAll(removeables);
    }
    private void createSQLPrepare(ETLRow row) {
        if (null != row.getColumns()) {
            for (ETLColumn c : row.getColumns()) {
                Object newValue = SqlUtils.stringToSqlValue(c.getFinalValue(), c.getFinalType(), c.isRequired(), true);
                Object oldValue = SqlUtils.stringToSqlValue(c.getOldValue(), c.getFinalType(), c.isRequired(), true);
                if (c.isKey()) {
                    row.getSqlKeys().put(c.getFinalName(), new ImmutablePair<>(oldValue, newValue));
                } else {
                    row.getSqlColumns().put(c.getFinalName(), new ImmutablePair<>(oldValue, newValue));
                }
            }
        }
    }

    private Table findTable(DbDialect targetDialect, String finalSchema, String finalTable) {
        Table table = null;
        try {
            table = targetDialect.findTable(finalSchema, finalTable);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询目标仓库表结构{}.{}出错!", finalSchema, finalTable, e);
        }
        return table;
    }
}