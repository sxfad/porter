/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.event.etl;

import java.sql.Types;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 10:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 10:59
 */
public class ETLColumn {
    //原始字段名
    private final String name;
    //原始更新后值
    private final String newValue;
    //原始更新前值
    private final String oldValue;


    /**
     * 用户自定义转换插件
     * 可修改字段,用于最终目标端数据插入
     * 2018-03-08 18:00:00
     */

    //最终目标端字段名
    private String finalName;
    //最终目标端字段值
    private String finalValue;
    //最终目标端字段值
    private String finalOldValue;

    //是否主键
    private  boolean isKey = false;
    //是否必填
    private boolean required = false;
    //目标端类型 java.sql.Types
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
        this.finalOldValue = oldValue;
    }

    public boolean isKey() {
        return isKey;
    }

    public void setFinalName(String finalName) {
        this.finalName = finalName;
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

    public String getFinalOldValue() {
        return finalOldValue;
    }

    public void setFinalOldValue(String finalOldValue) {
        this.finalOldValue = finalOldValue;
    }
}
