/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import com.alibaba.fastjson.annotation.JSONField;
import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.alert.AlertStrategy;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:29
 */
public class AlertConfig {
    @JSONField(deserialize = false, serialize = false)

    @Getter @Setter private AlertStrategy strategy;
    @Getter @Setter private AlertReceiver[] receiver = new AlertReceiver[0];
    //告警客户端
    @Getter @Setter private Map<String, String> client;
}
