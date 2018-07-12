/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.common.client.impl;

import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.alert.AlertProviderFactory;
import cn.vbill.middleware.porter.common.alert.AlertReceiver;
import cn.vbill.middleware.porter.common.config.AlertConfig;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 17:01
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 17:01
 */
public class EmailClientTest {

    @BeforeClass
    public static void initSource() throws ClientConnectionException, ConfigParseException {
        AlertConfig config = new AlertConfig();
        config.setStrategy(AlertPlugin.EMAIL);
        config.setFrequencyOfSeconds(10);
        config.setClient(new HashMap<String, String>() {
            {
                put("host", "smtphm.qiye.163.com");
                put("username", "zhang_kw@suixingpay.com");
                put("password", "CtJd6vQZxGZDdr7w");
                put("smtpAuth", "true");
                put("smtpStarttlsEnable", "true");
                put("smtpStarttlsRequired", "false");
            }
        });
        AlertReceiver r = new AlertReceiver();
        r.setEmail("1335210101@qq.com");
        r.setPhone("13800138000");
        r.setRealName("样板告警人");
        config.setReceiver(new AlertReceiver[]{r});
        AlertProviderFactory.INSTANCE.initialize(config);
    }



    @Test
    @Ignore
    public void notice() {
        AlertProviderFactory.INSTANCE.notice("1", "1", null);
        AlertProviderFactory.INSTANCE.notice("1", "1", null);
    }
}
