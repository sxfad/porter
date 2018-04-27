/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 16:03
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.task;

import com.suixingpay.datas.common.config.TableMapperConfig;

import java.util.Arrays;
import java.util.Map;

/**
 * 原表到目标表的映射
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 16:03
 */
public class TableMapper {
    /**
     * 字段映射后，强制目标端字段和源端字段一致，否则任务抛出异常停止
     */
    private boolean forceMatched = false;
    private String[] schema;
    private String[] table;
    private String[] updateDate;
    private Map<String, String> column;

    public boolean isForceMatched() {
        return forceMatched;
    }

    public void setForceMatched(boolean forceMatched) {
        this.forceMatched = forceMatched;
    }

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

    public Map<String, String> getColumn() {
        return column;
    }

    public void setColumn(Map<String, String> column) {
        this.column = column;
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
        return sb.toString();
    }

    public String[] getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String[] updateDate) {
        this.updateDate = updateDate;
    }

    public static TableMapper fromConfig(TableMapperConfig config) {
        TableMapper mapper = new TableMapper();
        mapper.setColumn(config.getColumn());
        mapper.setSchema(config.getSchema());
        mapper.setTable(config.getTable());
        mapper.setUpdateDate(config.getUpdateDate());
        mapper.setForceMatched(config.isForceMatched());
        return mapper;
    }

    public TableMapper toUpperCase() {
        if (null != updateDate) {
            for (int i = 0; i < updateDate.length; i++) {
                updateDate[i] = updateDate[i].toUpperCase();
            }
        }

        if (null != table) {
            for (int i = 0; i < table.length; i++) {
                table[i] = table[i].toUpperCase();
            }
        }

        if (null != schema) {
            for (int i = 0; i < schema.length; i++) {
                schema[i] = schema[i].toUpperCase();
            }
        }

        if (null != column) {
            Arrays.stream(column.keySet().toArray(new String[0])).forEach(k -> {
                String v = column.get(k);
                column.remove(k);
                column.put(k.toUpperCase(), v.toUpperCase());
            });
        }
        return this;
    }



}
