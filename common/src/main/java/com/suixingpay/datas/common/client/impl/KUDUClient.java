/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.LoadClient;
import com.suixingpay.datas.common.client.MetaQueryClient;
import com.suixingpay.datas.common.config.source.KuduConfig;
import com.suixingpay.datas.common.db.meta.TableColumn;
import com.suixingpay.datas.common.db.meta.TableSchema;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.kudu.Common;
import org.apache.kudu.Schema;
import org.apache.kudu.client.CreateTableOptions;
import org.apache.kudu.client.Operation;
import org.apache.kudu.client.OperationResponse;
import org.apache.kudu.client.PartialRow;
import org.apache.kudu.client.KuduClient;
import org.apache.kudu.client.KuduException;
import org.apache.kudu.client.KuduSession;
import org.apache.kudu.client.KuduTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * kudu客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class KUDUClient extends AbstractClient<KuduConfig> implements LoadClient, MetaQueryClient {
    private final Map<List<String>, TableSchema> tables = new ConcurrentHashMap<>();


    @Getter private KuduClient client;


    public KUDUClient(KuduConfig config) {
        super(config);
    }

    @Override
    protected void doStart() {
        KuduConfig config = getConfig();
        client = new KuduClient.KuduClientBuilder(config.getServers()).workerCount(config.getWorkerCount()).build();
    }

    @Override
    protected void doShutdown() throws KuduException {
        client.close();
    }

    @Override
    public TableSchema getTable(String schema, String tableName) throws Exception {
        List<String> keyList = Arrays.asList(schema, tableName);
        //如果表存在
        if (client.tableExists(tableName)) {
            tables.computeIfAbsent(keyList, new Function<List<String>, TableSchema>() {
                //从代码块中抛出异常
                @SneakyThrows(Exception.class)
                public TableSchema apply(List<String> strings) {
                    KuduTable kuduTable = client.openTable(tableName);
                    Schema kuduTableSchema = kuduTable.getSchema();

                    TableSchema tableSchema = new TableSchema();
                    tableSchema.setSchemaName(schema);
                    tableSchema.setTableName(tableName);
                    kuduTableSchema.getColumns().forEach(c -> {
                        TableColumn column = new TableColumn();
                        column.setDefaultValue(String.valueOf(c.getDefaultValue()));
                        column.setName(c.getName());
                        column.setPrimaryKey(c.isKey());
                        column.setRequired(!c.isNullable());
                        column.setTypeCode(c.getType().getDataType().getNumber());
                        tableSchema.addColumn(column);
                    });
                    return tableSchema;
                }
            });
        }
        return null;
    }

    @Override
    public int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime) {
        throw new  UnsupportedOperationException("kudu暂不支持条件查询");
    }

    public int[] insert(String table, List<List<Triple<String, Integer, String>>> rows) throws KuduException {
        return operation(table, rows, OperationType.INSERT);
    }

    public int[] delete(String table, List<List<Triple<String, Integer, String>>> rows) throws KuduException {
        return operation(table, rows, OperationType.DELTE);
    }

    public int[] update(String table, List<List<Triple<String, Integer, String>>> rows) throws KuduException {
        return operation(table, rows, OperationType.UPDATE);
    }

    public int[] operation(String table, List<List<Triple<String, Integer, String>>> rows, OperationType type) throws KuduException {
        int[] result = new int[rows.size()];
        KuduSession session = client.newSession();
        try {
            KuduTable kuduTable = client.openTable(table);
            for (List<Triple<String, Integer, String>> r : rows) {
                Operation operation = null;
                switch (type) {
                    case DELTE:
                        operation = kuduTable.newDelete();
                        break;
                    case INSERT:
                        operation = kuduTable.newInsert();
                        break;
                    case UPDATE:
                        operation = kuduTable.newUpdate();
                        break;
                }

                operation = kuduTable.newDelete();
                PartialRow row = operation.getRow();
                buildRow(r, row);
                session.apply(operation);
            }
            List<OperationResponse> responses = session.flush();
            for (int i = 0; i < responses.size(); i++) {
                OperationResponse response = responses.get(i);
                result[i] =  response.hasRowError() ? 0 : 1;
            }
        } finally {
            if (null != session) session.close();
        }
        return result;
    }

    private void buildRow(List<Triple<String, Integer, String>> row, PartialRow partialRow) {
        row.forEach(c -> {
            switch (c.getMiddle()) {
                case Common.DataType.BINARY_VALUE:
                    partialRow.addBinary(c.getLeft(), c.getRight().getBytes());
                    break;
                case Common.DataType.BOOL_VALUE:
                    partialRow.addBoolean(c.getLeft(), Boolean.getBoolean(c.getRight()));
                    break;
                case Common.DataType.DOUBLE_VALUE:
                    partialRow.addDouble(c.getLeft(), Double.valueOf(c.getRight()));
                    break;
                case Common.DataType.FLOAT_VALUE:
                    partialRow.addFloat(c.getLeft(), Float.valueOf(c.getRight()));
                    break;
                case Common.DataType.INT8_VALUE | Common.DataType.UINT8_VALUE:
                    partialRow.addByte(c.getLeft(), Byte.valueOf(c.getRight()));
                    break;
                case Common.DataType.INT16_VALUE | Common.DataType.UINT16_VALUE:
                    partialRow.addShort(c.getLeft(), Short.valueOf(c.getRight()));
                    break;
                case Common.DataType.INT32_VALUE | Common.DataType.UINT32_VALUE:
                    partialRow.addInt(c.getLeft(), Integer.valueOf(c.getRight()));
                    break;
                case Common.DataType.INT64_VALUE | Common.DataType.UINT64_VALUE | Common.DataType.UNIXTIME_MICROS_VALUE:
                    partialRow.addLong(c.getLeft(), Long.valueOf(c.getRight()));
                    break;
                case Common.DataType.STRING_VALUE:
                    partialRow.addString(c.getLeft(), c.getRight());
                    break;
                default:
                    partialRow.addString(c.getLeft(), c.getRight());
            }
        });
    }

    public int[] truncate(String finalTableName) throws KuduException {
        KuduSession session = client.newSession();
        try {
            Schema schema = client.openTable(finalTableName).getSchema();
            //删除表
            client.deleteTable(finalTableName);
            //重新建表
            client.createTable(finalTableName, schema, new CreateTableOptions().setRangePartitionColumns(new ArrayList<String>() {
                {
                    schema.getColumns().forEach(c -> {
                        if (c.isKey()) {
                            add(c.getName());
                        }
                    });
                }
            }));
        } finally {
            if (null != session) session.close();
        }
        return new int[]{1};
    }

    private enum OperationType {
        DELTE, UPDATE, INSERT;
    }
}
