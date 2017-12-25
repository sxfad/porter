package com.suixingpay.datas.common.connector;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:29
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象Connector
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:29
 */
public abstract  class AbstractConnector  implements DataConnector {
    private static final Logger logger = LoggerFactory.getLogger(AbstractConnector.class);
    private AtomicBoolean connected = new AtomicBoolean(false);
    protected DataDriver driver;
    //默认为任务私有连接池
    private boolean privatePool = true;

    public AbstractConnector(DataDriver driver) {
        this.driver = driver;
    }

    @Override
    public  boolean isConnected() {
        return connected.get() && doIsConnected();
    }
    public abstract boolean doIsConnected();
    @Override
    public void connect() {
        if (connected.compareAndSet(false, true)) {
            try {
                doConnect();
            } catch (Exception e) {
                logger.error("DataConnector  disconnected with error", e);
                disconnect();
            }
        } else {
            logger.info("DataConnector is already connected");
        }
    }

    @Override
    public void reconnect(){
        disconnect();
        connect();
    }

    @Override
    public void disconnect() {
        if (connected.compareAndSet(true, false)) {
            try {
                doDisconnect();
            } catch (Exception e) {
            }
        } else {
            logger.info("DataConnector is not connected");
        }
    }

    protected abstract void doDisconnect();

    protected abstract void doConnect();

    @Override
    public void setPrivatePool(boolean isPrivate) {
        this.privatePool = isPrivate;
    }

    @Override
    public boolean isPrivatePool() {
        return privatePool;
    }
}
