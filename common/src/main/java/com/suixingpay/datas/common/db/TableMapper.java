/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 16:03
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.db;

import java.util.Map;

/**
 * 原表到目标表的映射
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 16:03
 */
public class TableMapper {
    private boolean custom = false;
    private String[] schema;
    private String[] table;
    private String[] updateDate;
    private Map<String, String> columns;

    public String[] getSchema() {
        return schema;
    }

    public void setSchema(String[] schema) {
        this.schema = schema;
    }

    public String[] getTable() {
        return table;
    }

    public void setTable(String[] table) {
        this.table = table;
    }

    public Map<String, String> getColumns() {
        return columns;
    }

    public void setColumns(Map<String, String> columns) {
        this.columns = columns;
    }

    public boolean isCustom() {
        boolean changed = (null != schema && schema.length == 2)
                || (null != table && table.length ==2)
                || (null != updateDate && updateDate.length ==2)
                || (null != columns && !columns.isEmpty());
        return changed || custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }

    public String getUniqueKey(String taskId) {
        StringBuffer sb = new StringBuffer();
        sb.append(taskId).append("_");
        if (null != schema && schema.length == 2) {
            sb.append(schema[0]);
        }
        sb.append("_");
        if (null != table && table.length == 2) {
            sb.append(table[0]);
        }
        return isCustom() ? sb.toString().toUpperCase() :null;
    }

    public String[] getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String[] updateDate) {
        this.updateDate = updateDate;
    }
}
