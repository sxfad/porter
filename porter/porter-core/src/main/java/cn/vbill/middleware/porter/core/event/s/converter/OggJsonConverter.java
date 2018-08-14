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

package cn.vbill.middleware.porter.core.event.s.converter;

import cn.vbill.middleware.porter.common.consumer.Position;
import cn.vbill.middleware.porter.common.dic.ConsumeConverterPlugin;
import cn.vbill.middleware.porter.core.event.s.EventConverter;
import cn.vbill.middleware.porter.core.event.s.EventType;
import cn.vbill.middleware.porter.core.event.s.MessageEvent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:50
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 11:50
 */
public class OggJsonConverter implements EventConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(OggJsonConverter.class);

    private DateFormat opTsF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private DateFormat ctsf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");

    @Override
    public String getName() {
        return ConsumeConverterPlugin.OGG_JSON.getCode();
    }

    @Override
    public MessageEvent convert(Object... params) {
        Long consumerTime = (Long) params[0];
        Position position = (Position) params[1];
        JSONObject obj = JSON.parseObject((String) params[2]);

        EventType eventType = EventType.type(obj.getString("op_type"));
        //不能解析的事件跳过
        if (null == eventType ||  eventType == EventType.UNKNOWN) {
            return null;
        }
        //body
        MessageEvent event = new MessageEvent();
        String schemaAndTable = obj.getString("table");
        String[] stTmp = null != schemaAndTable ? schemaAndTable.split("\\.") : null;
        if (null != stTmp && stTmp.length == 2) {
            event.setSchema(stTmp[0]);
            event.setTable(stTmp[1]);
        }
        event.setOpType(eventType);
        try {
            String poTS = obj.getString("op_ts");
            event.setOpTs(opTsF.parse(poTS.substring(0, poTS.length() - 3)));
        } catch (Exception e) {
            LOGGER.error("%s", e);
        }

        try {
            String currentTS = obj.getString("current_ts");
            event.setCurrentTs(ctsf.parse(currentTS.substring(0, currentTS.length() - 3)));
        } catch (Exception e) {
            LOGGER.error("%s", e);
        }

        JSONArray pkeys = obj.containsKey("primary_keys") ? obj.getJSONArray("primary_keys") : null;
        if (null != pkeys) {
            event.setPrimaryKeys(pkeys.toJavaList(String.class));
        }
        event.setBefore(obj.getObject("before", Map.class));
        event.setAfter(obj.getObject("after", Map.class));
        event.setRowPosition(position);
        event.setBucketPosition(event.getRowPosition());
        event.setConsumerTime(consumerTime);
        event.setConsumedTime(System.currentTimeMillis());
        return event;
    }
}
