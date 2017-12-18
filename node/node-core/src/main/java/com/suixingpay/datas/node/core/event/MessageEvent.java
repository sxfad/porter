package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.Date;
import java.util.Map;

/**
 * 消息事件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:22
 */
public class MessageEvent {
    private EventHeader head;
    private String schema;
    private String table;
    //操作类型 I U D T
    private EventType opType;
    //操作时间
    private Date opTs;
    //解析事件的时间
    private Date currentTs;
    //修改之后的值
    private Map<String,String> after;
    //修改之前的值
    private Map<String,String> before;
    private String[] primaryKeys;

    public EventHeader getHead() {
        return head;
    }

    public void setHead(EventHeader head) {
        this.head = head;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public EventType getOpType() {
        return opType;
    }

    public void setOpType(EventType opType) {
        this.opType = opType;
    }

    public Date getOpTs() {
        return opTs;
    }

    public void setOpTs(Date opTs) {
        this.opTs = opTs;
    }

    public Date getCurrentTs() {
        return currentTs;
    }

    public void setCurrentTs(Date currentTs) {
        this.currentTs = currentTs;
    }

    public Map<String, String> getAfter() {
        return after;
    }

    public void setAfter(Map<String, String> after) {
        this.after = after;
    }

    public Map<String, String> getBefore() {
        return before;
    }

    public void setBefore(Map<String, String> before) {
        this.before = before;
    }

    public String[] getPrimaryKeys() {
        return primaryKeys;
    }

    public void setPrimaryKeys(String[] primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
