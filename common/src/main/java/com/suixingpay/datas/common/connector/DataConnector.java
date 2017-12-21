package com.suixingpay.datas.common.connector;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:23
 */
public interface DataConnector {
    boolean isPrivatePool();
    void setPrivatePool(boolean isPrivate);
    boolean isConnected();
    void connect();
    void disconnect();
    void reconnect();
}
