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

package cn.vbill.middleware.porter.plugin.loader.kafka.loader;


import cn.vbill.middleware.porter.common.task.exception.TaskDataException;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.core.task.setl.ETLBucket;
import cn.vbill.middleware.porter.core.task.setl.ETLColumn;
import cn.vbill.middleware.porter.core.task.loader.AbstractDataLoader;
import cn.vbill.middleware.porter.core.task.statistics.DSubmitStatObject;
import cn.vbill.middleware.porter.plugin.loader.kafka.KafkaLoaderConst;
import cn.vbill.middleware.porter.plugin.loader.kafka.client.KafkaProduceClient;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.core.task.setl.ETLRow;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
@SuppressWarnings("unchecked")
public class KafkaLoader extends AbstractDataLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaLoader.class);
    private static final FastDateFormat OP_TS_F = FastDateFormat.getInstance("yyyy-MM-dd hh:mm:ss.SSS");
    private static final FastDateFormat C_TS_F = FastDateFormat.getInstance("yyyy-MM-dd'T'hh:mm:ss.SSS000");
    @Override
    protected String getPluginName() {
        return KafkaLoaderConst.LOADER_PLUGIN_NAME.getCode();
    }

    @Override
    public Pair<Boolean, List<DSubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        return storeData(bucket, true);
    }

    protected Pair<Boolean, List<DSubmitStatObject>> storeData(ETLBucket bucket, boolean sync) throws TaskStopTriggerException {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        KafkaProduceClient client = getLoadClient();
        List<Triple<String, String, Integer>> producerRecords = new ArrayList<>();
        List<DSubmitStatObject> affectRow = new ArrayList<>();
        for (ETLRow row : bucket.getRows()) {
            String key = KafkaETLRowField.getRecordKey(row);
            String value = KafkaETLRowField.getRecordValue(row);
            producerRecords.add(new ImmutableTriple<>(key, value, null));
            //插入影响行数
            affectRow.add(new DSubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
                    1, row.getPosition(), row.getOpTime()));
        }
        client.send(producerRecords, sync);
        return new ImmutablePair(Boolean.TRUE, affectRow);
    }

    @Override
    public void mouldRow(ETLRow row) throws TaskDataException {
        KafkaProduceClient client = getLoadClient();
        //计算分区key
        StringBuilder keyBuilder = new StringBuilder();
        List<String> partitionKey = client.getPartitionKey(row.getFinalSchema(), row.getFinalTable());
        Stream<ETLColumn> filteredStream = null;
        if (null == partitionKey || partitionKey.isEmpty()) {
            //默认根据主键分区
            filteredStream = row.getColumns().stream().filter(c -> c.isKey());
        } else {
            //根据目标端配置指定分片key
            filteredStream = row.getColumns().stream().filter(c -> partitionKey.contains(c.getFinalName()));
        }
        if (null != filteredStream) filteredStream.sorted(Comparator.comparing(ETLColumn::getFinalName))
                .forEach(c -> keyBuilder.append(c.getFinalValue()).append("_"));

        KafkaETLRowField.setRecordKey(row, keyBuilder.toString());
        //转换成目标端格式
        if (client.renderOggJson()) {
            KafkaETLRowField.setRecordValue(row, formatOggRow(row));
        } else {
            KafkaETLRowField.setRecordValue(row, formatRow(row));
        }
    }

    @Override
    public String getDefaultClientType() {
        return KafkaLoaderConst.LOADER_SOURCE_TYPE_NAME.getCode();
    }

    /**
     * 自定义扩展字段
     */
    protected static class KafkaETLRowField {
        private static String RECORD_KEY = "key";
        private static String RECORD_VALUE = "value";
        private static String RECORD_PARTITION = "partition";
        private static String FORMATTED_FIELD = "formattedData";

        protected static Map<String, Object> getData(ETLRow row) {
            return (Map<String, Object>) row.getExtendsField().computeIfAbsent(FORMATTED_FIELD, k -> new HashMap<>());
        }

        protected static void setRecordKey(ETLRow row, String key) {
            getData(row).put(RECORD_KEY, key);
        }

        protected static void setRecordValue(ETLRow row, String value) {
            getData(row).put(RECORD_VALUE, value);
        }

        protected static String getRecordKey(ETLRow row) {
            return (String) getData(row).getOrDefault(RECORD_KEY, null);
        }

        protected static String getRecordValue(ETLRow row) {
            return (String) getData(row).getOrDefault(RECORD_VALUE, null);
        }

    }

    public String formatRow(ETLRow row) {
        JSONObject formattedRow = new JSONObject();
        formattedRow.put("schema", row.getFinalSchema());
        formattedRow.put("table", row.getFinalTable());
        formattedRow.put("storeTime", row.getConsumerTime());
        formattedRow.put("opType", row.getFinalOpType().getValue());
        JSONArray columns = new JSONArray();
        JSONArray keys = new JSONArray();
        formattedRow.fluentPut("keys", keys).fluentPut("columns", columns);
        row.getColumns().forEach(c -> {
            JSONObject object = new JSONObject();
            object.put("name", c.getFinalName());
            object.put("before", c.getFinalOldValue());
            object.put("after", c.getFinalValue());
            object.put("afterMissing", c.isFinalAfterMissing());
            object.put("beforeMissing", c.isFinalBeforeMissing());
            object.put("isKey", c.isKey());
            columns.add(object);
            if (c.isKey()) keys.add(c.getFinalName());
        });
        return formattedRow.toJSONString();
    }

    public String formatOggRow(ETLRow row) {
        JSONObject formattedRow = new JSONObject();
        formattedRow.put("table", row.getFinalSchema() + "." + row.getFinalTable());
        formattedRow.put("op_ts", OP_TS_F.format(new Date(row.getConsumerTime())));
        formattedRow.put("current_ts", C_TS_F.format(new Date()));
        formattedRow.put("op_type", row.getFinalOpType().getValue());
        JSONObject before = new JSONObject();
        JSONObject after = new JSONObject();
        JSONArray keys = new JSONArray();
        formattedRow.fluentPut("primary_keys", keys).fluentPut("before", before).fluentPut("after", after);
        row.getColumns().stream().filter(c -> c.isKey()).forEach(c -> {
            keys.add(c.getFinalName());
        });
        row.getColumns().stream().filter(c -> !c.isFinalBeforeMissing()).forEach(c -> {
            before.put(c.getFinalName(), c.getFinalOldValue());
        });

        row.getColumns().stream().filter(c -> !c.isFinalAfterMissing()).forEach(c -> {
            after.put(c.getFinalName(), c.getFinalValue());
        });

        return formattedRow.toJSONString();
    }
}
