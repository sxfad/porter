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

package cn.vbill.middleware.porter.plugin.converter.canal;

import cn.vbill.middleware.porter.common.task.consumer.Position;
import cn.vbill.middleware.porter.core.message.converter.EventConverter;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import com.alibaba.otter.canal.protocol.CanalEntry;

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

    public static final String CONVERTER_NAME = "canalRow";

    @Override
    public String getName() {
        return CONVERTER_NAME;
    }

    @Override
    public List<MessageEvent> convertList(Object... params) {
        Position bucketPosition = (Position) params[0];
        Position rowPosition = (Position) params[1];
        CanalEntry.Entry entry = (CanalEntry.Entry) params[2];
        //非row data
        if (entry.getEntryType() != CanalEntry.EntryType.ROWDATA) {
            return null;

        }
        CanalEntry.RowChange rowChange = null;
        try {
            rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
        } catch (Exception e) {
            return null;
        }
        //query
        CanalEntry.EventType eventType = rowChange.getEventType();
        if (eventType == CanalEntry.EventType.QUERY) {
            return null;
        }
        List<MessageEvent> events = new ArrayList<>();

        Date opTs = new Date(entry.getHeader().getExecuteTime());
        Date currentTs = new Date();
        String schema = entry.getHeader().getSchemaName();
        String table = entry.getHeader().getTableName();
        for (CanalEntry.RowData rowData : rowChange.getRowDatasList()) {
            MessageEvent event = new MessageEvent();
            event.setBucketPosition(bucketPosition);
            event.setRowPosition(rowPosition);
            event.setTable(table);
            event.setSchema(schema);
            event.setCurrentTs(currentTs);
            event.setOpTs(opTs);
            event.setConsumerTime(entry.getHeader().hasExecuteTime() ? entry.getHeader().getExecuteTime() : -1);
            event.setConsumedTime(System.currentTimeMillis());
            if (eventType == CanalEntry.EventType.DELETE) {
                event.setOpType(MessageAction.DELETE);
                rowData.getBeforeColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }

                    event.getBefore().put(c.getName(), c.getIsNull() ? null : c.getValue());
                });
            } else if (eventType == CanalEntry.EventType.INSERT) {
                event.setOpType(MessageAction.INSERT);
                rowData.getAfterColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }
                    event.getAfter().put(c.getName(), c.getIsNull() ? null : c.getValue());
                });

            } else if (eventType == CanalEntry.EventType.UPDATE) {
                event.setOpType(MessageAction.UPDATE);

                //before
                rowData.getBeforeColumnsList().forEach(c -> {
                    if (c.getIsKey()) {
                        event.getPrimaryKeys().add(c.getName());
                    }
                    event.getBefore().put(c.getName(), c.getIsNull() ? null : c.getValue());
                });

                //after
                rowData.getAfterColumnsList().forEach(c -> {
                    event.getAfter().put(c.getName(), c.getIsNull() ? null : c.getValue());
                });
            }
            events.add(event);
        }
        return events;
    }
}
