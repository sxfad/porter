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

package cn.vbill.middleware.porter.common.db;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月01日 16:08
 */
public enum  SqlErrorCode {
    //ORA-00904: "C3": 标识符无效
    ERROR_904(904),
    //ORA-00942: 表或视图不存在
    ERROR_942(942),
    //HY000: Field 'c3' doesn't have a default value
    ERROR_1364(1364),
    //ORA-01438: 值大于为此列指定的允许精度
    ERROR_1438(1438),
    //字段长度与插入内容不符
    ERROR_12899(12899),
    //ORA-01400: cannot insert NULL into
    ERROR_1400(1400),
    //errorCode 17002, state 08006
    //IO Error: The Network Adapter could not establish the connection
    ERROR_17002(17002);

    public final int code;
    SqlErrorCode(int code) {
        this.code = code;
    }
}
