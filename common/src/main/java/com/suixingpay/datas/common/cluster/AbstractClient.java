/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:44
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster;


import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 17:44
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 17:44
 */
public abstract class AbstractClient implements Client {
    private AtomicBoolean connected = new AtomicBoolean(false);
    @Override
    public void connect() {
        if (connected.compareAndSet(false, true)) {
            try {
                doConnect();
            } catch (Exception e) {
                disconnect();
            }
        } else {

        }
    }

    protected abstract void doConnect() throws IOException;
    @Override
    public void disconnect() {
        if (connected.compareAndSet(true, false)) {
            try {
                doDisconnect();
            } catch (Exception e) {
            }
        } else {

        }
    }

    protected abstract void doDisconnect() throws InterruptedException;

    @Override
    public boolean isConnected() {
        return connected.get() && doIsConnected();
    }

    protected abstract boolean doIsConnected();
}
