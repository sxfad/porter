/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.db.meta;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 16:07
 */
public class TableSchema {
    @Setter @Getter private String schemaName;
    @Setter @Getter private String tableName;
    private Map<String, TableColumn> columns = new HashMap<>();

    public TableColumn findColumn(String columnsName) {
        return columns.getOrDefault(columnsName, null);
    }

    public List<TableColumn> getColumns() {
        return columns.values().stream().collect(Collectors.toList());
    }

    public void addColumn(TableColumn column) {
        columns.put(column.getName(), column);
    }


    public TableSchema toUpperCase() {
        this.schemaName = schemaName.toUpperCase();
        this.tableName = tableName.toUpperCase();
        Arrays.stream(columns.keySet().toArray()).forEach(k -> {
            TableColumn column = columns.get(k);
            column.setName(column.getName().toUpperCase());
            //删除旧key
            columns.remove(k);
            //添加新key
            columns.put(k.toString().toUpperCase(), column);
        });
        return this;
    }
}
