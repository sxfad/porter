/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.plugin.loader.kudu.client;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.task.loader.LoadClient;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.plugin.PluginServiceClient;
import cn.vbill.middleware.porter.common.util.db.meta.TableColumn;
import cn.vbill.middleware.porter.common.util.db.meta.TableSchema;
import cn.vbill.middleware.porter.plugin.loader.kudu.config.KuduConfig;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.kudu.ColumnSchema;
import org.apache.kudu.Common;
import org.apache.kudu.Schema;
import org.apache.kudu.client.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;


/**
 * kudu客户端
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 15:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 15:14
 */
public class KUDUClient extends AbstractClient<KuduConfig> implements PluginServiceClient, LoadClient, MetaQueryClient {
    private final Map<List<String>, TableSchema> tables = new ConcurrentHashMap<>();

    @Getter
    private KuduClient client;

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
    public final TableSchema getTable(String schema, String tableName) throws Exception {
        List<String> keyList = Arrays.asList(schema, tableName);
        String newTableName = getTableName(schema, tableName);
        //如果表存在
        if (client.tableExists(newTableName)) {
            return tables.computeIfAbsent(keyList, new Function<List<String>, TableSchema>() {
                //从代码块中抛出异常
                @SneakyThrows(Exception.class)
                public TableSchema apply(List<String> strings) {
                    KuduTable kuduTable = client.openTable(newTableName);
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
        throw new UnsupportedOperationException("kudu暂不支持条件查询");
    }

    /**
     * insert
     *
     * @param schemaName
     * @param table
     * @param rows
     * @return
     * @throws KuduException
     */
    public int[] insert(String schemaName, String table, List<List<Triple<String, Integer, String>>> rows) throws TaskStopTriggerException {
        return operation(schemaName, table, rows, OperationType.INSERT);
    }

    /**
     * delete
     *
     * @param schemaName
     * @param table
     * @param rows
     * @return
     * @throws KuduException
     */
    public int[] delete(String schemaName, String table, List<List<Triple<String, Integer, String>>> rows) throws TaskStopTriggerException {
        return operation(schemaName, table, rows, OperationType.DELETE);
    }

    /**
     * update
     *
     * @param schemaName
     * @param table
     * @param rows
     * @return
     * @throws KuduException
     */
    public int[] update(String schemaName, String table, List<List<Triple<String, Integer, String>>> rows) throws TaskStopTriggerException {
        return operation(schemaName, table, rows, OperationType.UPDATE);
    }

    /**
     * truncate
     *
     * @param schemaName
     * @param finalTableName
     * @return
     * @throws KuduException
     */
    public int[] truncate(String schemaName, String finalTableName) throws TaskStopTriggerException {
        KuduSession session = client.newSession();
        try {
            Schema schema = client.openTable(getTableName(schemaName, finalTableName)).getSchema();
            //删除表
            client.deleteTable(finalTableName);

            ArrayList<String> list = new ArrayList<>();
            for (ColumnSchema columnSchema : schema.getColumns()) {
                if (columnSchema.isKey()) {
                    list.add(columnSchema.getName());
                }
            }

            //重新建表
            client.createTable(finalTableName, schema, new CreateTableOptions().setRangePartitionColumns(list));
        } catch (KuduException e) {
            throw new TaskStopTriggerException(e);
        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (KuduException e) {
                }
            }
        }
        return new int[]{1};
    }

    /**
     * operation
     *
     * @param schema
     * @param table
     * @param rows
     * @param type
     * @return
     * @throws KuduException
     */
    public int[] operation(String schema, String table, List<List<Triple<String, Integer, String>>> rows, OperationType type) throws TaskStopTriggerException {
        int[] result = new int[rows.size()];
        KuduSession session = client.newSession();
        //同步刷新
        session.setFlushMode(SessionConfiguration.FlushMode.AUTO_FLUSH_SYNC);
        try {
            KuduTable kuduTable = client.openTable(getTableName(schema, table));
            for (int i = 0; i < rows.size(); i++) {
                List<Triple<String, Integer, String>> r = rows.get(i);
                Operation operation = null;
                switch (type) {
                    case DELETE:
                        operation = kuduTable.newDelete();
                        break;
                    case INSERT:
                        operation = kuduTable.newInsert();
                        break;
                    case UPDATE:
                        operation = kuduTable.newUpdate();
                        break;
                    default:
                        throw new UnsupportedOperationException("unsupported operation:" + type);
                }

                PartialRow row = operation.getRow();
                buildRow(r, row);
                OperationResponse response = session.apply(operation);
                result[i] = response.hasRowError() ? 0 : 1;
            }
            session.flush();
        } catch (KuduException e) {
            throw new TaskStopTriggerException(e);
        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (KuduException e) {
                }
            }
        }
        return result;
    }

    /**
     * buildRow
     *
     * @param row
     * @param partialRow
     */
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



    private enum OperationType {
        DELETE, UPDATE, INSERT;
    }

    /**
     * getTableName
     *
     * @param schema
     * @param table
     * @return
     */
    private String getTableName(String schema, String table) {
        StringBuilder nameBuilder = new StringBuilder();
        schema = StringUtils.trimToEmpty(schema);
        if (!StringUtils.isBlank(schema)) {
            nameBuilder.append(schema);
            nameBuilder.append(".");
        }
        nameBuilder.append(table);
        return nameBuilder.toString();
    }

    @Override
    public String getClientInfo() {
        KuduConfig config = getConfig();
        return new StringBuilder().append("数据库地址->").append(config.getServers()).toString();
    }
}
