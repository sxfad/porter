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

package cn.vbill.middleware.porter.common.cluster.impl.standalone;

import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.client.DistributedLock;
import cn.vbill.middleware.porter.common.client.impl.FileClient;
import cn.vbill.middleware.porter.common.cluster.ClusterMonitor;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterProvider;
import cn.vbill.middleware.porter.common.config.ClusterConfig;
import cn.vbill.middleware.porter.common.config.source.FileOperationConfig;
import cn.vbill.middleware.porter.common.dic.ClusterPlugin;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;

/**
 * 单机版实现
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:15
 * @version: V1.0
 * @review: zkevin/2018年10月19日 14:15
 */
public class StandaloneProvider extends AbstractClusterProvider {
    @Override
    protected ClusterPlugin getMatchType() {
        return ClusterPlugin.STANDALONE;
    }

    @Override
    protected ClusterMonitor newMonitor() {
        return new StandaloneMonitor();
    }

    @Override
    protected Class getClusterListenerClass() {
        return StandaloneListener.class;
    }

    @Override
    protected Client initClient(ClusterConfig clusterConfig) throws ConfigParseException {
        return new FileClient(new FileOperationConfig(clusterConfig.getClient()).stuff());
    }

    @Override
    protected DistributedLock initiateLock(ClusterClient client) {
        throw new UnsupportedOperationException();
    }
}
