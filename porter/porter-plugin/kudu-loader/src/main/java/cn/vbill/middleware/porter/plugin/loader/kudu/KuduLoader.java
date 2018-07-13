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

package cn.vbill.middleware.porter.plugin.loader.kudu;

import cn.vbill.middleware.porter.common.exception.TaskDataException;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.event.s.EventType;
import cn.vbill.middleware.porter.common.client.impl.KUDUClient;
import cn.vbill.middleware.porter.common.dic.LoaderPlugin;
import cn.vbill.middleware.porter.core.event.etl.ETLBucket;
import cn.vbill.middleware.porter.core.event.etl.ETLColumn;
import cn.vbill.middleware.porter.core.event.etl.ETLRow;
import cn.vbill.middleware.porter.core.loader.AbstractDataLoader;
import cn.vbill.middleware.porter.core.loader.SubmitStatObject;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:57
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:57
 */
public class KuduLoader extends AbstractDataLoader {

    @Override
    protected String getPluginName() {
        return LoaderPlugin.KUDU_NATIVE.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        List<SubmitStatObject> affectRow = new ArrayList<>();
        KUDUClient client = getLoadClient();
        bucket.getBatchRows().forEach(new Consumer<List<ETLRow>>() {
            @Override
            @SneakyThrows(TaskStopTriggerException.class)
            public void accept(List<ETLRow> l) {
                if (l.isEmpty()) return;
                //批次操作类型
                EventType type = l.get(0).getFinalOpType();
                String tableName = l.get(0).getFinalTable();
                String schemaName = l.get(0).getFinalSchema();

                //所有字段
                List<List<Triple<String, Integer, String>>> rows = new ArrayList<>();
                //主键字段
                List<List<Triple<String, Integer, String>>> keyRows = new ArrayList<>();
                l.forEach(r -> {
                    List<Triple<String, Integer, String>> row = new ArrayList<>();
                    row.addAll(KuduCustomETLRowField.getKeys(r));
                    row.addAll(KuduCustomETLRowField.getColumns(r));
                    rows.add(row);
                    keyRows.add(KuduCustomETLRowField.getKeys(r));
                });
                int[] result = new int[0];
                try {
                    switch (type) {
                        case INSERT:
                            result = client.insert(schemaName, tableName, rows);
                            break;
                        case UPDATE:
                            result = new int[l.size()];
                            for (int i = 0; i < l.size(); i++) {
                                ETLRow r = l.get(i);
                                //如果主键存在变更
                                if (KuduCustomETLRowField.isKeyChanged(r)) {
                                    client.delete(schemaName, tableName, Arrays.asList(KuduCustomETLRowField.getOldKeys(r)));
                                    //主键+非主键
                                    List<Triple<String, Integer, String>> row = new ArrayList<>();
                                    row.addAll(KuduCustomETLRowField.getKeys(r));
                                    row.addAll(KuduCustomETLRowField.getColumns(r));
                                    result = client.insert(schemaName, tableName, Arrays.asList(row));
                                } else {
                                    List<Triple<String, Integer, String>> row = new ArrayList<>();
                                    row.addAll(KuduCustomETLRowField.getKeys(r));
                                    row.addAll(KuduCustomETLRowField.getColumns(r));
                                    result = client.update(schemaName, tableName, Arrays.asList(row));
                                }
                            }
                            break;
                        case DELETE:
                            result = client.delete(schemaName, tableName, keyRows);
                            break;
                        case TRUNCATE:
                            result = client.truncate(schemaName, tableName);
                            break;
                        default:

                    }

                    //更新进度信息
                    for (int affect = 0; affect < l.size(); affect++) {
                        ETLRow row = l.get(affect);
                        affectRow.add(new SubmitStatObject(schemaName, tableName, type,
                                result[affect], row.getPosition(), row.getOpTime()));
                    }

                } catch (Exception e) {
                    throw new TaskStopTriggerException(e);
                }
            }
        });
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }

    @Override
    public void mouldRow(ETLRow row) throws TaskDataException {
        if (null != row.getColumns()) {
            List<Triple<String, Integer, String>> oldKeys = KuduCustomETLRowField.getOldKeys(row);
            List<Triple<String, Integer, String>> keys = KuduCustomETLRowField.getKeys(row);
            List<Triple<String, Integer, String>> columns = KuduCustomETLRowField.getColumns(row);
            for (ETLColumn c : row.getColumns()) {
                if (c.isKey()) {
                    keys.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalValue()));
                    //更新时需要，判断主键是否发生变化
                    if (!c.getFinalOldValue().equals(c.getFinalValue()) && row.getFinalOpType() == EventType.UPDATE) {
                        KuduCustomETLRowField.setKeyChanged(row, true);
                        oldKeys.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalOldValue()));
                    }
                } else {
                    columns.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalValue()));
                }
            }
        }
    }

    /**
     * 自定义扩展字段
     */
    protected static class KuduCustomETLRowField {

        private  static String KEY_FIELD = "kuduKey";
        private  static String OLD_KEY_FIELD = "oldKuduKey";
        private  static String COLUMN_FIELD = "kuduColumn";
        private  static String IS_KEY_CHANGED_FIELD = "kuduKeyChanged";

        protected static List<Triple<String, Integer, String>> getKeys(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(KEY_FIELD, k -> new ArrayList<>());
        }

        protected static List<Triple<String, Integer, String>> getOldKeys(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(OLD_KEY_FIELD, k -> new ArrayList<>());
        }

        protected static List<Triple<String, Integer, String>> getColumns(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(COLUMN_FIELD, k -> new ArrayList<>());
        }

        protected static Boolean isKeyChanged(ETLRow row) {
            return (Boolean) row.getExtendsField().computeIfAbsent(IS_KEY_CHANGED_FIELD, k -> Boolean.FALSE);
        }

        protected static Boolean setKeyChanged(ETLRow row, Boolean isChanged) {
            return (Boolean) row.getExtendsField().put(IS_KEY_CHANGED_FIELD, isChanged);
        }
    }

}
