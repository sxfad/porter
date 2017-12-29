/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:29
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.datasource;

import java.util.UUID;
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
public abstract  class AbstractSourceWrapper implements DataSourceWrapper {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSourceWrapper.class);
    private AtomicBoolean connected = new AtomicBoolean(false);
    protected DataDriver driver;
    //默认为任务私有连接池
    private boolean privatePool = true;
    //当前为对象创建时初始化，最终是配置中心读取配置
    private final String id;

    public AbstractSourceWrapper(DataDriver driver) {
        this.driver = driver;
        this.id = UUID.randomUUID().toString();
    }


    @Override
    public void destroy() {
        if (connected.compareAndSet(true, false)) {
            try {
                doDestroy();
            } catch (Exception e) {
            }
        } else {
            logger.info("DataSourceWrapper is not created");
        }
    }

    @Override
    public void create() {
        if (connected.compareAndSet(false, true)) {
            try {
                doCreate();
            } catch (Exception e) {
            }
        } else {
            logger.info("DataSourceWrapper was  created already");
        }
    }

    protected abstract void doDestroy();
    protected abstract void doCreate();

    @Override
    public void setPrivatePool(boolean isPrivate) {
        this.privatePool = isPrivate;
    }

    @Override
    public boolean isPrivatePool() {
        return privatePool;
    }

    @Override
    public String getUniqueId() {
        return id;
    }
}
