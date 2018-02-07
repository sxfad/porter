/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 00:10
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 00:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 00:10
 */
public class EmailAlertConfig extends Config {
    private static final String RECEIVER_SPLIT_CHARACTER = ",";
    @Getter @Setter private String[] receiver;
    @Getter @Setter private String sender;

    public EmailAlertConfig() {
        configType = ConfigType.EMAIL_ALERT;
    }

    @Override
    protected void childStuff() {
        receiver = getProperties().getOrDefault("receiver", "").split(RECEIVER_SPLIT_CHARACTER);
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"receiver"};
    }
}
