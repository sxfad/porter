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

package cn.vbill.middleware.porter.core.message;

/**
 * 时间类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月06日 11:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月06日 11:53
 */
public enum MessageAction {

    /**
     * insert
     */
    INSERT(0, "INSERT"),

    /**
     * update
     */
    UPDATE(1, "UPDATE"),

    /**
     * delete
     */
    DELETE(2, "DELETE"),

    /**
     * transaction begin
     */
    TRANSACTION_BEGIN(3, "BEGIN"),

    /**
     * transaction end
     */
    TRANSACTION_END(4, "END"),

    /**
     * truncate
     */
    TRUNCATE(5, "TRUNCATE"),

    /**
     * unknown
     */
    UNKNOWN(-1, "UNKNOWN");
    private int index;
    private String value;

    /**
     * insert index
     */
    public static final int INSERT_INDEX = 0;

    /**
     * update index
     */
    public static final int UPDATE_INDEX = 1;

    /**
     * delete index
     */
    public static final int DELETE_INDEX = 2;

    /**
     * begin index
     */
    public static final int BEGIN_INDEX = 3;

    /**
     * end index
     */
    public static final int END_INDEX = 4;

    /**
     * truncate index
     */
    public static final int TRUNCATE_INDEX = 5;
    MessageAction(int index, String value) {
        this.index =  index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getValue() {
        return value;
    }
}
