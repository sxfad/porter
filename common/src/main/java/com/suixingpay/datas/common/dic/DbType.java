/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 16:27
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * 关系数据库类型
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 16:27
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 16:27
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum  DbType {

    MYSQL("MYSQL", "MYSQL"), ORACLE("ORACLE", "ORACLE");

    @Getter private final String code;
    @Getter private final String name;

    public static final List<DbType> TYPES = new ArrayList<DbType>() {
        {
            add(MYSQL);
            add(ORACLE);
        }
    };
}
