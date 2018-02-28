/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 14:16
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.alert.provider;

import com.suixingpay.datas.common.alert.AlertReceiver;

import java.util.List;

public interface AlertProvider {
    boolean notice(String title, String notice, List<AlertReceiver> receivers);
    boolean notice(String title, String notice);
}
