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

package cn.vbill.middleware.porter.common.client;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共客户端资源容器
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:37
 */
public enum PublicClientContext {

    /**
     * INSTANCE
     */
    INSTANCE();
    private final Map<String, Client> allSources = new ConcurrentHashMap<>();

    /**
     * 初始化公用DataSource
     *
     * @param configs
     */
    public void initialize(List<Pair<String, SourceConfig>> configs) throws Exception {
        for (Pair<String, SourceConfig> p : configs) {
            Client client = AbstractClient.getClient(p.getRight());
            client.setPublic(true);
            client.start();
            allSources.put(p.getLeft(), client);
        }
    }

    /**
     * 获取Source
     *
     * @date 2018/8/10 下午2:59
     * @param: [sourceName]
     * @return: cn.vbill.middleware.porter.common.client.Client
     */
    public Client getSource(String sourceName) {
        return allSources.getOrDefault(sourceName, null);
    }
}
