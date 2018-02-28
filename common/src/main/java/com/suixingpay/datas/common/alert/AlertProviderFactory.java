/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert;

import com.suixingpay.datas.common.alert.provider.AlertProvider;
import com.suixingpay.datas.common.alert.provider.NormalAlertProvider;
import com.suixingpay.datas.common.client.AlertClient;
import com.suixingpay.datas.common.client.impl.EmailClient;
import com.suixingpay.datas.common.config.AlertConfig;
import com.suixingpay.datas.common.config.source.EmailConfig;
import com.suixingpay.datas.common.exception.ClientConnectionException;
import com.suixingpay.datas.common.exception.ConfigParseException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:14
 */
public enum AlertProviderFactory {
    INSTANCE();
    private final ReadWriteLock initializedLock = new ReentrantReadWriteLock();
    private AlertProvider alert;
    public void initialize(AlertConfig config) throws ConfigParseException, ClientConnectionException {
        //校验配置文件参数
        if (null == config || null == config.getStrategy() || null == config.getClient()
                || config.getClient().isEmpty()) {
            return;
        }

        initializedLock.writeLock().lock();
        try {
            if (config.getStrategy() == AlertStrategy.EMAIL) {
                AlertClient client = new EmailClient(new EmailConfig(config.getClient()).stuff(), config.getReceiver());
                alert = new NormalAlertProvider(client);
            }
        } finally {
            initializedLock.writeLock().unlock();
        }
    }

    public void notice(String title, String msg, List<AlertReceiver> receiverList) {
        try {
            if (initializedLock.readLock().tryLock(5, TimeUnit.SECONDS)) {
                if (null != alert) alert.notice(title, msg, receiverList);
                initializedLock.readLock().unlock();
            }
        } catch (InterruptedException e) {

        }
    }
}
