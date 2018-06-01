/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 11:50
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.event.s.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.consumer.Position;
import com.suixingpay.datas.common.dic.ConsumeConverterPlugin;
import com.suixingpay.datas.node.core.event.s.EventConverter;
import com.suixingpay.datas.node.core.event.s.EventType;
import com.suixingpay.datas.node.core.event.s.MessageEvent;
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
    private static final DateFormat OP_TS_F = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
    private static final DateFormat C_TS_F = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
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
        if (null == eventType ||  eventType == EventType.UNKNOWN) return null;
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
            event.setOpTs(OP_TS_F.parse(poTS.substring(0, poTS.length() - 3)));
        } catch (Exception e) {

        }

        try {
            String currentTS = obj.getString("current_ts");
            event.setCurrentTs(C_TS_F.parse(currentTS.substring(0, currentTS.length() - 3)));
        } catch (Exception e) {

        }

        JSONArray pkeys = obj.containsKey("primary_keys") ? obj.getJSONArray("primary_keys") : null;
        if (null != pkeys) event.setPrimaryKeys(pkeys.toJavaList(String.class));
        event.setBefore(obj.getObject("before", Map.class));
        event.setAfter(obj.getObject("after", Map.class));
        event.setRowPosition(position);
        event.setBucketPosition(event.getRowPosition());
        event.setConsumerTime(consumerTime);
        event.setConsumedTime(System.currentTimeMillis());
        return event;
    }
}
