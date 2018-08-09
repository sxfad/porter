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

package cn.vbill.middleware.porter.core.event.etl;

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
    //before字段值没提供
    private final boolean beforeMissing;
    //after字段值没提供
    private final boolean afterMissing;

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

    //before字段值没提供
    private boolean finalBeforeMissing;
    //after字段值没提供
    private boolean finalAfterMissing;

    //是否主键
    private  boolean isKey = false;
    //是否必填
    private boolean required = false;
    //目标端类型 java.sql.Types
    private int finalType;

    /**
     * 默认字段是发生变化的
     * @param name
     * @param newValue
     * @param oldValue
     * @param finalValue
     * @param isKey
     */
    public ETLColumn(String name, String newValue, String oldValue, String finalValue, boolean isKey) {
        this(false, false, name, newValue, oldValue, finalValue, isKey, isKey, Types.VARCHAR);
    }

    /**
     * 接受外部定义字段是否发生变化
     * @param beforeMissing
     * @param afterMissing
     * @param name
     * @param newValue
     * @param oldValue
     * @param finalValue
     * @param isKey
     */
    public ETLColumn(boolean beforeMissing, boolean afterMissing, String name, String newValue, String oldValue, String finalValue, boolean isKey) {
        this(beforeMissing, afterMissing, name, newValue, oldValue, finalValue, isKey, isKey, Types.VARCHAR);
    }

    public ETLColumn(boolean beforeMissing, boolean afterMissing, String name, String newValue, String oldValue, String finalValue, boolean isKey,
                     boolean required, int type) {
        this.name = name;
        this.newValue = newValue;
        this.oldValue = oldValue;
        this.beforeMissing = beforeMissing;
        this.afterMissing = afterMissing;
        this.isKey = isKey;
        this.required = required;
        this.finalType = type;
        this.finalName = this.name;
        this.finalValue = finalValue;
        this.finalOldValue = this.oldValue;
        this.finalBeforeMissing = this.beforeMissing;
        this.finalAfterMissing = this.afterMissing;
    }

    /**
     * 大写
     *
     * @date 2018/8/8 下午5:54
     * @param: []
     * @return: cn.vbill.middleware.porter.core.event.etl.ETLColumn
     */
    public ETLColumn toUpperCase() {
        this.finalName = this.finalName.toUpperCase();
        return this;
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

    public boolean isFinalBeforeMissing() {
        return finalBeforeMissing;
    }

    public void setFinalBeforeMissing(boolean finalBeforeMissing) {
        this.finalBeforeMissing = finalBeforeMissing;
    }

    public boolean isFinalAfterMissing() {
        return finalAfterMissing;
    }

    public void setFinalAfterMissing(boolean finalAfterMissing) {
        this.finalAfterMissing = finalAfterMissing;
    }
}
