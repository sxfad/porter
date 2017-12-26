package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月25日 18:41
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.Date;
import java.util.List;

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
    private boolean hasChangedTable = false;
    private boolean hasChangedSchema = false;
    private String finalSchema;
    private String finalTable;
    //操作类型 I U D T
    private final EventType opType;
    private final List<ETLColumn> columns;
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

    public boolean isHasChangedTable() {
        return hasChangedTable;
    }

    public void setHasChangedTable(boolean hasChangedTable) {
        this.hasChangedTable = hasChangedTable;
    }

    public boolean isHasChangedSchema() {
        return hasChangedSchema;
    }

    public void setHasChangedSchema(boolean hasChangedSchema) {
        this.hasChangedSchema = hasChangedSchema;
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
}
