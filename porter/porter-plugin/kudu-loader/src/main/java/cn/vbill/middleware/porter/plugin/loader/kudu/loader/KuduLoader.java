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

package cn.vbill.middleware.porter.plugin.loader.kudu.loader;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import cn.vbill.middleware.porter.core.task.loader.AbstractDataLoader;
import cn.vbill.middleware.porter.core.task.statistics.DSubmitStatObject;
import cn.vbill.middleware.porter.plugin.loader.kudu.KuduLoaderConst;
import cn.vbill.middleware.porter.plugin.loader.kudu.client.KUDUClient;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 11:57
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 11:57
 */
@SuppressWarnings("unchecked")
public class KuduLoader extends AbstractDataLoader {

    @Override
    protected String getPluginName() {
        return KuduLoaderConst.LOADER_PLUGIN_NAME.getCode();
    }

    @Override
    public Pair<Boolean, List<DSubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        List<DSubmitStatObject> affectRow = new ArrayList<>();
        KUDUClient client = getLoadClient();
        for (ETLRow r : bucket.getRows()) {
            int[] result = new int[0];
            switch (r.getFinalOpType()) {
                case INSERT:
                    result = client.insert(r.getFinalSchema(), r.getFinalTable(), Arrays.asList(KuduCustomETLRowField.getKeys(r),
                            KuduCustomETLRowField.getColumns(r)));
                    break;
                case UPDATE:
                    //如果主键存在变更
                    if (KuduCustomETLRowField.isKeyChanged(r)) {
                        client.delete(r.getFinalSchema(), r.getFinalTable(), Arrays.asList(KuduCustomETLRowField.getOldKeys(r)));
                        //主键+非主键
                        result = client.insert(r.getFinalSchema(), r.getFinalTable(), Arrays.asList(KuduCustomETLRowField.getKeys(r),
                                KuduCustomETLRowField.getColumns(r)));
                    } else {
                        result = client.update(r.getFinalSchema(), r.getFinalTable(), Arrays.asList(KuduCustomETLRowField.getKeys(r),
                                KuduCustomETLRowField.getColumns(r)));
                    }
                    break;
                case DELETE:
                    result = client.delete(r.getFinalSchema(), r.getFinalTable(), Arrays.asList(KuduCustomETLRowField.getKeys(r)));
                    break;
                case TRUNCATE:
                    result = client.truncate(r.getFinalSchema(), r.getFinalTable());
                    break;
                default:
            }
            //更新进度信息
            affectRow.add(new DSubmitStatObject(r.getFinalSchema(), r.getFinalTable(), r.getFinalOpType(), result[0], r.getPosition(), r.getOpTime()));
        }
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }

    @Override
    public void mouldRow(ETLRow row) {
        if (null != row.getColumns()) {
            List<Triple<String, Integer, String>> oldKeys = KuduCustomETLRowField.getOldKeys(row);
            List<Triple<String, Integer, String>> keys = KuduCustomETLRowField.getKeys(row);
            List<Triple<String, Integer, String>> columns = KuduCustomETLRowField.getColumns(row);
            for (ETLColumn c : row.getColumns()) {
                if (c.isKey()) {
                    keys.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalValue()));
                    //更新时需要，判断主键是否发生变化
                    if (!c.getFinalOldValue().equals(c.getFinalValue()) && row.getFinalOpType() == MessageAction.UPDATE) {
                        KuduCustomETLRowField.setKeyChanged(row, true);
                        oldKeys.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalOldValue()));
                    }
                } else {
                    columns.add(new ImmutableTriple<>(c.getFinalName(), c.getFinalType(), c.getFinalValue()));
                }
            }
        }
    }

    @Override
    public String getDefaultClientType() {
        return KuduLoaderConst.LOADER_SOURCE_TYPE_NAME.getCode();
    }

    /**
     * 自定义扩展字段
     */
    protected static class KuduCustomETLRowField {

        private  static String KEY_FIELD = "kuduKey";
        private  static String OLD_KEY_FIELD = "oldKuduKey";
        private  static String COLUMN_FIELD = "kuduColumn";
        private  static String IS_KEY_CHANGED_FIELD = "kuduKeyChanged";

        /**
         * getKeys
         * @param row
         * @return
         */
        protected static List<Triple<String, Integer, String>> getKeys(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(KEY_FIELD, k -> new ArrayList<>());
        }

        /**
         * getOldKeys
         * @param row
         * @return
         */
        protected static List<Triple<String, Integer, String>> getOldKeys(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(OLD_KEY_FIELD, k -> new ArrayList<>());
        }

        /**
         * getColumns
         * @param row
         * @return
         */
        protected static List<Triple<String, Integer, String>> getColumns(ETLRow row) {
            return (List<Triple<String, Integer, String>>) row.getExtendsField().computeIfAbsent(COLUMN_FIELD, k -> new ArrayList<>());
        }

        /**
         * isKeyChanged
         * @param row
         * @return
         */
        protected static Boolean isKeyChanged(ETLRow row) {
            return (Boolean) row.getExtendsField().computeIfAbsent(IS_KEY_CHANGED_FIELD, k -> Boolean.FALSE);
        }

        /**
         * setKeyChanged
         * @param row
         * @param isChanged
         * @return
         */
        protected static Boolean setKeyChanged(ETLRow row, Boolean isChanged) {
            return (Boolean) row.getExtendsField().put(IS_KEY_CHANGED_FIELD, isChanged);
        }
    }

}
