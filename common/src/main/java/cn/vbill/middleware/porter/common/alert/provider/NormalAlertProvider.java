/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.alert.provider;

import cn.vbill.middleware.porter.common.client.AlertClient;
import cn.vbill.middleware.porter.common.alert.AlertReceiver;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月30日 11:47
 */
public class NormalAlertProvider implements  AlertProvider {
    private final AlertClient client;
    public  NormalAlertProvider(AlertClient client) {
        this.client = client;

    }

    public boolean notice(String title, String notice, List<AlertReceiver> receivers) {
        client.send(notice, title, receivers);
        return true;
    }

    public boolean notice(String title, String notice) {
        return notice(title, notice, null);
    }
}
