/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:25
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.config.ClusterConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.task.TaskEventListener;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:25
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 11:25
 */
public enum ClusterProviderProxy {
    INSTANCE();
    private final AtomicBoolean isConfig = new AtomicBoolean(false);
    private volatile ClusterProvider provider;

    public void initialize(ClusterConfig config) throws IOException, ClientException, ConfigParseException {
        if (isConfig.compareAndSet(false, true)) {
            List<ClusterProvider> providers = SpringFactoriesLoader.loadFactories(ClusterProvider.class, null);

            for (ClusterProvider tmp : providers) {
                if (tmp.matches(config.getStrategy())) {
                    tmp.start(config);
                    provider = tmp;
                    break;
                }
            }
        }
    }


    public void broadcast(ClusterCommand command) throws Exception {
        provider.broadcastCommand(command);
    }

    public void stop() {
        provider.stop();
    }

    public void addTaskListener(TaskEventListener listener) {
        provider.addTaskEventListener(listener);
    }
}
