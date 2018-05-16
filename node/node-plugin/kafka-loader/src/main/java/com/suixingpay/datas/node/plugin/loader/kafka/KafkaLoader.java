/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.plugin.loader.kafka;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.client.impl.KafkaProduceClient;
import com.suixingpay.datas.common.dic.LoaderPlugin;
import com.suixingpay.datas.common.exception.TaskDataException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.core.event.etl.ETLColumn;
import com.suixingpay.datas.node.core.event.etl.ETLRow;
import com.suixingpay.datas.node.core.loader.AbstractDataLoader;
import com.suixingpay.datas.node.core.loader.SubmitStatObject;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Stream;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 11:53
 */
public class KafkaLoader extends AbstractDataLoader {

    @Override
    protected String getPluginName() {
        return LoaderPlugin.KAFKA_SYNC.getCode();
    }

    @Override
    public Pair<Boolean, List<SubmitStatObject>> load(ETLBucket bucket) throws TaskStopTriggerException {
        return storeData(bucket, true);
    }

    protected Pair<Boolean, List<SubmitStatObject>> storeData(ETLBucket bucket, boolean sync) throws TaskStopTriggerException {
        LOGGER.info("start loading bucket:{},size:{}", bucket.getSequence(), bucket.getRows().size());
        KafkaProduceClient client = getLoadClient();
        List<Triple<String, String, Integer>> producerRecords = new ArrayList<>();
        List<SubmitStatObject> affectRow = new ArrayList<>();
        for (ETLRow row : bucket.getRows()) {
            String key = KafkaETLRowField.getRecordKey(row);
            String value = KafkaETLRowField.getRecordValue(row);
            producerRecords.add(new ImmutableTriple<>(key, value, null));
            //插入影响行数
            affectRow.add(new SubmitStatObject(row.getFinalSchema(), row.getFinalTable(), row.getFinalOpType(),
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
        KafkaETLRowField.setRecordValue(row, formatRow(row));
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
}
