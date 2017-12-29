package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.sql.Types;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 10:59
 */
public class ETLColumn {
    private final String name;
    private final String newValue;
    private final String oldValue;
    private  boolean isKey = false;
    private boolean required = false;
    private String finalName;
    private String finalValue;
    private int finalType;

    public ETLColumn(String name, String newValue, String oldValue, String finalValue, boolean isKey) {
        this(name, newValue, oldValue, finalValue, isKey, isKey, Types.VARCHAR);
    }

    public ETLColumn(String name, String newValue, String oldValue, String finalValue, boolean isKey, boolean required, int type) {
        this.name = name;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.isKey = isKey;
        this.required = required;
        this.finalType = type;
        this.finalName = name;
        this.finalValue = finalValue;
    }

    public String getName() {
        return name;
    }

    public String getNewValue() {
        return newValue;
    }

    public String getOldValue() {
        return oldValue;
    }

    public boolean isKey() {
        return isKey;
    }

    public String isFinalName() {
        return finalName;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
    }

    public String isFinalValue() {
        return finalValue;
    }

    public void setFinalValue(String finalValue) {
        this.finalValue = finalValue;
    }

    public int getFinalType() {
        return finalType;
    }

    public void setFinalType(int finalType) {
        this.finalType = finalType;
    }

    public String getFinalName() {
        return finalName;
    }

    public void setKey(boolean key) {
        isKey = key;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getFinalValue() {
        return finalValue;
    }
}
