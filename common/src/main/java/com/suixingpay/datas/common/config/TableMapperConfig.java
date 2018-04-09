/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 19:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 19:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 19:07
 */
public class TableMapperConfig {
    @Getter @Setter private boolean auto = true;
    //忽略目标端大小写
    @Getter @Setter private boolean ignoreTargetCase = true;
    @Getter @Setter private String[] schema;
    @Getter @Setter private String[] table;
    @Getter @Setter private String[] updateDate;
    @Getter @Setter private Map<String, String> column;
}
