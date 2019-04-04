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

package cn.vbill.middleware.porter.common.cluster.config;

import cn.vbill.middleware.porter.common.cluster.dic.ClusterPlugin;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 14:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 14:22
 */
public class ClusterConfig {
    private ClusterPlugin strategy;
    //集群客户端
    private Map<String, String> client;

    //统计上传客户端
    private Map<String, String> statistic;

    public ClusterPlugin getStrategy() {
        return strategy;
    }

    public void setStrategy(ClusterPlugin strategy) {
        this.strategy = strategy;
    }

    public Map<String, String> getClient() {
        return client;
    }

    public void setClient(Map<String, String> client) {
        this.client = client;
    }

    public Map<String, String> getStatistic() {
        return statistic;
    }

    public void setStatistic(Map<String, String> statistic) {
        this.statistic = statistic;
    }
}
