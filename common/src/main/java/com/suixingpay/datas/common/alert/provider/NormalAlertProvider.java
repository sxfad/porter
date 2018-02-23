/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert.provider;

import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.client.AlertClient;
import com.suixingpay.datas.common.util.MachineUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月30日 11:47
 */
public class NormalAlertProvider implements  AlertProvider{
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final AlertClient client;
    public  NormalAlertProvider(AlertClient client) {
        this.client = client;

    }
    public boolean notice(String notice, List<AlertReceiver> receivers) {
        String title = "[" + DATE_FORMAT.format(new Date()) + "][" + MachineUtils.localhost() + ":" + MachineUtils.getPID() + "]数据同步告警";
        client.send(notice, title, receivers);
        return true;
    }

    public boolean notice(String notice) {
        return notice(notice, null);
    }
}
