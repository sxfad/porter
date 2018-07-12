/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.db.meta;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 16:07
 */
public class TableSchema {
    @Setter @Getter private boolean noPrimaryKey = false;
    @Setter @Getter private String schemaName;
    @Setter @Getter private String tableName;
    private final Map<String, TableColumn> columns = new ConcurrentHashMap<>();
    private final AtomicBoolean isUpperCased = new AtomicBoolean(false);
    //定义门栓,仅控制toUpperCase多线程访问逻辑
    private final CountDownLatch  upperCased = new CountDownLatch(1);
    public TableColumn findColumn(String columnsName) {
        return columns.getOrDefault(columnsName, null);
    }


    public List<TableColumn> getColumns() {
        return Collections.unmodifiableList(columns.values().stream().collect(Collectors.toList()));
    }

    public void addColumn(TableColumn column) {
        columns.put(column.getName(), column);
    }


    public final TableSchema toUpperCase() throws InterruptedException {
        //在if逻辑里打开门闩
        if (!isUpperCased.get() && isUpperCased.compareAndSet(false, true)) {
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
            upperCased.countDown();
        }
        //所有调用该方法的线程，等待toUpperCase逻辑完成才能继续往下执行，确保方法安全的发布
        upperCased.await();
        return this;
    }
}
