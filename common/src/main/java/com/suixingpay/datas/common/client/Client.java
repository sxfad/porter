/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:00
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.exception.ClientConnectionException;


/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:00
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:00
 */
public interface Client {
    void start() throws Exception;
    void shutdown() throws Exception;
    boolean isStarted();
    <T> T getConfig();

    void setPublic(boolean isPublic);
    default boolean isPublic() {
        return false;
    }

    default void testConnection() throws ClientConnectionException {

    }
}
