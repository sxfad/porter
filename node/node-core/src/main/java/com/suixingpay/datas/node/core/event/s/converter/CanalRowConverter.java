/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:50
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.event.s.converter;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.suixingpay.datas.common.dic.ConsumeConverterPlugin;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.core.event.s.MessageEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:50
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 11:50
 */
public class CanalRowConverter implements EventConverter {
    @Override
    public String getName() {
        return ConsumeConverterPlugin.CANAL_ROW.getCode();
    }

    @Override
    public <T> List<MessageEvent> convertList(Object... params) {
        JSONObject position = (JSONObject) params[0];
        CanalEntry.Entry entry = (CanalEntry.Entry) params[1];
        //非row data
        if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA) return null;

        CanalEntry.RowChange rowChange = null;
        try {
            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            return null;
        }

        //query
        CanalEntry.EventType eventType = rowChange.getEventType();
        if (eventType == CanalEntry.EventType.QUERY) return null;

        List<MessageEvent> events = new ArrayList<>();

        Date opTs = new Date(entry.getHeader().getExecuteTime());
        Date currentTs = new Date();
        String schema = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();

        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            MessageEvent event = new MessageEvent();
            event.setPosition(position.toJSONString());
            event.setTable(table);
            event.setSchema(schema);
            event.setCurrentTs(currentTs);
            event.setOpTs(opTs);

            if (eventType == CanalEntry.EventType.DELETE) {
                event.setOpType(EventType.DELETE);
                rowData.getBeforeColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }
                    event.getBefore().put(c.getName(), c.getValue());
                });
            } else if (eventType == CanalEntry.EventType.INSERT) {
                event.setOpType(EventType.INSERT);
                rowData.getAfterColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }
                    event.getAfter().put(c.getName(), c.getValue());
                });

            } else if (eventType == CanalEntry.EventType.UPDATE) {
                event.setOpType(EventType.UPDATE);

                //before
                rowData.getBeforeColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }
                    event.getBefore().put(c.getName(), c.getValue());
                });

                //after
                rowData.getAfterColumnsList().forEach(c -> {
                    event.getAfter().put(c.getName(), c.getValue());
                });
            }
            events.add(event);
        }
        return events;
    }
}
