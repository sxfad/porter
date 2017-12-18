package com.suixingpay.datas.common.cluster;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:00
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:00
 */
public interface Client {
    void connect();
    void disconnect();
    boolean isConnected();

}
