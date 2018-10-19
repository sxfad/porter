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
import cn.vbill.middleware.porter.common.client.impl.FileClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterMonitor;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:18
 * @version: V1.0
 * @review: zkevin/2018年10月19日 14:18
 */
public class StandaloneMonitor extends AbstractClusterMonitor {
    private FileClient client;
    @Override
    public void doStart() {
        try {
            boolean preStat = client.exists(StandaloneListener.PREFIX_ATALOG, false);
            if (!preStat) {
                client.createDir(StandaloneListener.PREFIX_ATALOG);
            }
            boolean stat = client.exists(StandaloneListener.BASE_CATALOG, false);
            if (!stat) {
                client.createDir(StandaloneListener.BASE_CATALOG);
            }
            for (ClusterListener listener : listeners.values()) {
                StandaloneListener zkListener = null;
                try {
                    zkListener = (StandaloneListener) listener;
                    logger.info("init:{},watch:{}", zkListener.listenPath(), false);
                    client.createDir(zkListener.listenPath());
                    logger.info("attempted create node:{}", zkListener.listenPath());
                } catch (Throwable e) {
                    logger.warn("初始化standalone节点{}监听失败", (null != zkListener ? zkListener.listenPath() : "---"), e);
                }
            }
        } catch (Throwable e) {
            logger.error("启动StandaloneMonitor异常", e);
        }
    }

    @Override
    public void setClient(Client client) {
        this.client = (FileClient) client;
    }
}
