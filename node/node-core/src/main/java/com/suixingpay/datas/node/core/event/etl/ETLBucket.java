package com.suixingpay.datas.node.core.event.etl;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 11:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 11:04
 */
public class ETLBucket {
    private static final Logger LOGGER = LoggerFactory.getLogger(ETLBucket.class);
    private final long sequence;
    private final String dataSourceId;
    private final List<ETLRow> rows;

    public ETLBucket(long sequence, List<ETLRow> rows, String dataSourceId) {
        this.sequence = sequence;
        this.rows = rows;
        this.dataSourceId = dataSourceId;
    }

    public long getSequence() {
        return sequence;
    }

    public List<ETLRow> getRows() {
        return rows;
    }

    /**
     * 转换数据模型
     * @param events
     * @return
     */
    public static ETLBucket from(Pair<Long, List<MessageEvent>> events, String dataSourceId) {
        List<ETLRow> rows = new ArrayList<>();
        for (MessageEvent event : events.getRight()) {
            LOGGER.debug(JSON.toJSONString(event));
            List<ETLColumn> columns = new ArrayList<>();
            Boolean loopAfter = ! event.getAfter().isEmpty();
            for (Map.Entry<String,Object> entity : loopAfter ? event.getAfter().entrySet() : event.getBefore().entrySet()) {
                Object newValue = "";
                Object oldValue = "";

                if (loopAfter) {
                    newValue = entity.getValue();
                    oldValue = event.getBefore().getOrDefault(entity.getKey(),null);
                } else {
                    newValue = event.getBefore().getOrDefault(entity.getKey(),null);
                    oldValue = entity.getValue();
                }
                Object finalValue = newValue;
                if (event.getOpType() == EventType.DELETE) {
                    finalValue = oldValue;
                }

                String newValueStr= String.valueOf(newValue);
                newValueStr = newValueStr.equals("null") ? null : newValueStr;
                String oldValueStr= String.valueOf(oldValue);
                oldValueStr = oldValueStr.equals("null") ? null : oldValueStr;
                String finalValueStr= String.valueOf(finalValue);
                finalValueStr = finalValueStr.equals("null") ? null : finalValueStr;

                //源数据事件精度损失，转字符串也会有精度损失。后续观察处理
                ETLColumn column = new ETLColumn(entity.getKey(), newValueStr, oldValueStr, finalValueStr, event.getPrimaryKeys().contains(entity.getKey()));
                columns.add(column);
            }

            ETLRow row = new ETLRow(event.getSchema(), event.getTable(), event.getOpType(), columns, event.getOpTs());
            row.setIndex(event.getHead().getOffset()+"");
            rows.add(row);
            LOGGER.debug(JSON.toJSONString(row));
        }
        return new ETLBucket(events.getKey(), rows, dataSourceId);
    }

    public String getDataSourceId() {
        return dataSourceId;
    }
}
