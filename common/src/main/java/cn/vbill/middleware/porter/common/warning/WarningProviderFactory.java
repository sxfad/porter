/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.warning;

import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import cn.vbill.middleware.porter.common.warning.provider.WarningProvider;
import cn.vbill.middleware.porter.common.warning.provider.NormalWarningProvider;
import cn.vbill.middleware.porter.common.warning.client.WarningClient;
import cn.vbill.middleware.porter.common.warning.client.EmailClient;
import cn.vbill.middleware.porter.common.warning.config.WarningConfig;
import cn.vbill.middleware.porter.common.warning.config.EmailConfig;
import cn.vbill.middleware.porter.common.warning.entity.WarningPlugin;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 20:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 20:14
 */
public enum WarningProviderFactory {

    /**
     * INSTANCE
     *
     * @date 2018/8/10 下午2:46
     * @param:
     * @return:
     */
    INSTANCE();
    private final ReadWriteLock initializedLock = new ReentrantReadWriteLock();
    private volatile WarningProvider alert;

    /**
     * initialize
     *
     * @date 2018/8/10 下午2:46
     * @param: [config]
     * @return: void
     */
    public void initialize(WarningConfig config) throws ConfigParseException, ClientConnectionException, InterruptedException {
        //校验配置文件参数
        if (null == config || null == config.getStrategy() || null == config.getClient()
                || config.getClient().isEmpty()) {
            return;
        }

        if (initializedLock.writeLock().tryLock(1, TimeUnit.MINUTES)) {
            try {
                if (config.getStrategy() == WarningPlugin.EMAIL) {
                    WarningClient client = new EmailClient(new EmailConfig(config.getClient()).stuff(), config.getFrequencyOfSeconds());
                    alert = new NormalWarningProvider(client);
                }
            } finally {
                initializedLock.writeLock().unlock();
            }
        }
    }

    /**
     * notice
     *
     * @date 2018/8/10 下午2:46
     * @param: [title, msg, receiverList]
     * @return: void
     */
    public void notice(WarningMessage message, WarningReceiver... receiver) throws InterruptedException {
        if (initializedLock.readLock().tryLock(1, TimeUnit.MINUTES)) {
            try {
                if (null != alert) {
                    alert.notice(message, receiver);
                }
            } finally {
                initializedLock.readLock().unlock();
            }
        }
    }
}
