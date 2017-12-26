package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 10:59
 */
public class ETLColumn {
    private final String name;
    private final Object newValue;
    private final Object oldValue;
    private Class javaType;
    private final boolean isKey;

    private boolean hasChangedName;
    private boolean hasChangedValue;
    private boolean finalName;
    private boolean finalValue;
    private Class finalJavaType;

    public ETLColumn(String name, Object newValue, Object oldValue, Class javaType, boolean isKey) {
        this.name = name;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.isKey = isKey;
    }

    public String getName() {
        return name;
    }

    public Object getNewValue() {
        return newValue;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public Class getJavaType() {
        return javaType;
    }

    public boolean isKey() {
        return isKey;
    }

    public boolean isHasChangedName() {
        return hasChangedName;
    }

    public void setHasChangedName(boolean hasChangedName) {
        this.hasChangedName = hasChangedName;
    }

    public boolean isHasChangedValue() {
        return hasChangedValue;
    }

    public void setHasChangedValue(boolean hasChangedValue) {
        this.hasChangedValue = hasChangedValue;
    }

    public boolean isFinalName() {
        return finalName;
    }

    public void setFinalName(boolean finalName) {
        this.finalName = finalName;
    }

    public boolean isFinalValue() {
        return finalValue;
    }

    public void setFinalValue(boolean finalValue) {
        this.finalValue = finalValue;
    }

    public Class getFinalJavaType() {
        return finalJavaType;
    }

    public void setFinalJavaType(Class finalJavaType) {
        this.finalJavaType = finalJavaType;
    }

    public void setJavaType(Class javaType) {
        this.javaType = javaType;
    }
}
