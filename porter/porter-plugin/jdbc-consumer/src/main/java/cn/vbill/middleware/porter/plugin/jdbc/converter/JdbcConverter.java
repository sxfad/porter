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

package cn.vbill.middleware.porter.plugin.jdbc.converter;

import cn.vbill.middleware.porter.common.task.consumer.Position;
import cn.vbill.middleware.porter.core.message.MessageAction;
import cn.vbill.middleware.porter.core.message.MessageEvent;
import cn.vbill.middleware.porter.core.message.converter.EventConverter;
import cn.vbill.middleware.porter.plugin.connector.jdbc.client.JdbcClient;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:50
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 11:50
 */
public class JdbcConverter implements EventConverter {

    public static final String CONVERTER_NAME = "jdbcConnector";

    @Override
    public String getName() {
        return CONVERTER_NAME;
    }

    @Override
    public MessageEvent convert(Object... params) {
        Position position = (Position) params[0];
        JdbcClient.RowInfo row  = (JdbcClient.RowInfo) params[1];
        MessageEvent event = new MessageEvent();
        event.setBucketPosition(position);
        event.setRowPosition(position);
        event.setTable(row.getTable());
        event.setSchema(row.getSchema());
        event.setCurrentTs(row.getCurrentTime());
        event.setOpTs(row.getCurrentTime());
        event.setOpType(MessageAction.UPDATE);
        row.getColumns().forEach(c -> {
            if (c.isKey()) event.getPrimaryKeys().add(c.getColumnName());
            event.getAfter().put(c.getColumnName(), c.getValue());
        });
        return event;
    }

    @Override
    public List<MessageEvent> convertList(Object... params) {
        List<MessageEvent> events = new ArrayList<>();
        Position position = (Position) params[0];
        List<JdbcClient.RowInfo> rows  = (List<JdbcClient.RowInfo>) params[1];
        for (JdbcClient.RowInfo row : rows) {
            events.add(convert(position, row));
        }
        return events;
    }
}
