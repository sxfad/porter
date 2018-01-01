/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:40
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert;

import com.suixingpay.datas.common.alert.meta.AlertParams;
import com.suixingpay.datas.common.alert.meta.EmailParams;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:40
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月30日 11:40
 */
public enum AlertStrategyType {
    SMS(null),
    EMAIL(EmailParams.INSTANCE);
    private AlertParams meta;
    private AlertStrategyType(EmailParams paramMeta) {
        this.meta = paramMeta;
    }

    public AlertParams getMeta() {
        return meta;
    }
}
