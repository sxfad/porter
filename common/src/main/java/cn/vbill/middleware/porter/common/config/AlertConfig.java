/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config;

import java.util.Map;

import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.alert.AlertReceiver;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:29
 */
public class AlertConfig {
    
    
    public AlertConfig() {
        
    }
    
    public AlertConfig(AlertPlugin strategy, AlertReceiver[] receiver, Map<String, String> client) {
        this.strategy = strategy;
        this.receiver = receiver;
        this.client = client;
    }

    // second
    @Getter
    @Setter
    private Integer frequencyOfSeconds = 60;

    @Getter
    @Setter
    private AlertPlugin strategy;

    @Getter
    @Setter
    private AlertReceiver[] receiver = new AlertReceiver[0];

    // 告警客户端
    @Getter
    @Setter
    private Map<String, String> client;
}
