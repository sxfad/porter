/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 00:10
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 00:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 00:10
 */
public class EmailConfig extends SourceConfig {
    @Setter @Getter private String host;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private boolean smtpAuth = true;
    @Setter @Getter private boolean smtpStarttlsEnable = true;
    @Setter @Getter private boolean smtpStarttlsRequired = false;

    public EmailConfig() {
        sourceType = SourceType.EMAIL;
    }

    public EmailConfig(Map<String, String> properties) {
        this();
        super.setProperties(properties);
    }

    @Override
    protected void childStuff() {
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
