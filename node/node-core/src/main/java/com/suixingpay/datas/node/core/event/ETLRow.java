package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 18:41
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 将消息源事件转换为ETLRow
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 18:41
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月25日 18:41
 */
public class ETLRow {
    private final String schema;
    private final String table;
    private String finalSchema;
    private String finalTable;
    //操作类型 I U D T
    private final EventType opType;
    private final List<ETLColumn> columns;
    private Map<String, Pair<Object,Object>> sqlKeys = new LinkedHashMap<>();
    private Map<String, Pair<Object,Object>> sqlColumns = new LinkedHashMap<>();
    //操作时间，保留该字段可以在需要的时候计算出与最终执行时间间隔
    private final Date opTime;
    public ETLRow(String schema, String table, EventType opType, List<ETLColumn> columns, Date opTime) {
        this.schema = schema;
        this.table = table;
        this.opType = opType;
        this.columns = columns;
        this.opTime = opTime;

        //数据映射时使用
        this.finalSchema = schema;
        this.finalTable = table;
    }
    public String getSchema() {
        return schema;
    }

    public String getTable() {
        return table;
    }

    public EventType getOpType() {
        return opType;
    }

    public List<ETLColumn> getColumns() {
        return columns;
    }

    public Date getOpTime() {
        return opTime;
    }

    public String getFinalSchema() {
        return finalSchema;
    }

    public void setFinalSchema(String finalSchema) {
        this.finalSchema = finalSchema;
    }

    public String getFinalTable() {
        return finalTable;
    }

    public void setFinalTable(String finalTable) {
        this.finalTable = finalTable;
    }

    public Map<String, Pair<Object,Object>> getSqlKeys() {
        return sqlKeys;
    }

    public void setSqlKeys(Map<String, Pair<Object,Object>> sqlKeys) {
        this.sqlKeys = sqlKeys;
    }

    public Map<String, Pair<Object,Object>> getSqlColumns() {
        return sqlColumns;
    }

    public void setSqlColumns(Map<String, Pair<Object,Object>> sqlColumns) {
        this.sqlColumns = sqlColumns;
    }
}
