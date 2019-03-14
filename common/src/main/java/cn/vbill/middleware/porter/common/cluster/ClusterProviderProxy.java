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

package cn.vbill.middleware.porter.common.cluster;

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.config.ClusterConfig;
import cn.vbill.middleware.porter.common.task.TaskEventListener;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:25
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 11:25
 */
public enum ClusterProviderProxy {

    /**
     * INSTANCE
     */
    INSTANCE();
    private final AtomicBoolean isConfig = new AtomicBoolean(false);
    private volatile ClusterProvider provider;

    /**
     * initialize
     *
     * @param config
     * @throws Exception
     */
    public void initialize(ClusterConfig config) throws Exception {
        if (isConfig.compareAndSet(false, true)) {
            List<ClusterProvider> providers = SpringFactoriesLoader.loadFactories(ClusterProvider.class, JavaFileCompiler.getInstance());

            for (ClusterProvider tmp : providers) {
                if (tmp.matches(config.getStrategy())) {
                    tmp.start(config);
                    provider = tmp;
                    break;
                }
            }
        }
    }

    /**
     * broadcast
     *
     * @param command
     * @throws Exception
     */
    public void broadcastEvent(ClusterCommand command) throws Exception {
        provider.broadcastEvent(command);
    }

    public void registerClusterEvent(ClusterListenerEventExecutor eventExecutor) {
        provider.registerClusterEvent(eventExecutor);
    }

    public void runWithClusterEvent(BiConsumer<ClusterCommand, ClusterClient> block, ClusterCommand command) {
        provider.runWithClusterEvent(block, command);
    }

    /**
     * stop
     */
    public void stop() {
        provider.stop();
    }

    /**
     * addTaskListener
     *
     * @param listener
     */
    public void addTaskListener(TaskEventListener listener) {
        provider.addTaskEventListener(listener);
    }
}
