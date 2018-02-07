/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.event.s;

import java.util.*;

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
    private Map<String,Object> after;
    //修改之前的值
    private Map<String,Object> before;
    private List<String> primaryKeys;

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

    /**
     * 如果字段为空，构造空集合。方便后期对象操作
     * @return
     */
    public Map<String, Object> getAfter() {
        return null == after ? new HashMap<>(0) : after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }

    /**
     * 如果字段为空，构造空集合。方便后期对象操作
     * @return
     */
    public Map<String, Object> getBefore() {
        return null == before ? new HashMap<>(0) : before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }

    /**
     * 如果字段为空，构造空集合。方便后期对象操作
     * @return
     */
    public List<String> getPrimaryKeys() {
        return null == primaryKeys ? new ArrayList<>(0) : primaryKeys;
    }

    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }
}
