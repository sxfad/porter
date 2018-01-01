/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:06
 */
public class AlertDriver {
    //告警类型
    private AlertStrategyType alertWay;
    //扩展属性，与AlertStrategyType关联
    private Map<String,String> extend;

    public AlertStrategyType getAlertWay() {
        return alertWay;
    }

    public void setAlertWay(AlertStrategyType alertWay) {
        this.alertWay = alertWay;
    }

    public Map<String, String> getExtend() {
        return extend;
    }

    public void setExtend(Map<String, String> extend) {
        this.extend = extend;
    }
}
