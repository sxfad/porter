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

package cn.vbill.middleware.porter.core.event.s;

/**
 * 时间类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月06日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月06日 11:53
 */
public enum EventType {
    INSERT(0, "INSERT", "I"),
    UPDATE(1, "UPDATE", "U"),
    DELETE(2, "DELETE", "D"),
    TRANSACTION_BEGIN(3, "BEGIN", ""),
    TRANSACTION_END(4, "END", "END"),
    TRUNCATE(5, "TRUNCATE", "T"),
    UNKNOWN(-1, "UNKNOWN", "UNKNOWN");
    private int index;
    private String value;
    private String code;
    public static final int INSERT_INDEX = 0;
    public static final int UPDATE_INDEX = 1;
    public static final int DELETE_INDEX = 2;
    public static final int BEGIN_INDEX = 3;
    public static final int END_INDEX = 4;
    public static final int TRUNCATE_INDEX = 5;
    EventType(int index, String value, String code) {
        this.index =  index;
        this.value = value;
        this.code = code;
    }

    /**
     * 初始化type
     *
     * @date 2018/8/8 下午5:58
     * @param: [kafkaEvent]
     * @return: cn.vbill.middleware.porter.core.event.s.EventType
     */
    public static EventType type(String kafkaEvent) {
        if (kafkaEvent.equals("I")) {
            return  INSERT;
        } else if (kafkaEvent.equals("U")) {
            return  UPDATE;
        } else if (kafkaEvent.equals("D")) {
            return  DELETE;
        } else if (kafkaEvent.equals("BEGIN")) {
            return  TRANSACTION_BEGIN;
        } else if (kafkaEvent.equals("END")) {
            return  TRANSACTION_END;
        } else if (kafkaEvent.equals("T")) {
            return  TRUNCATE;
        } else {
            return UNKNOWN;
        }
    }
    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }
}
