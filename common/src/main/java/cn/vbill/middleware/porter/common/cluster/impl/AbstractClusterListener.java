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

package cn.vbill.middleware.porter.common.cluster.impl;

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年01月11日 14:51
 * @version: V1.0
 * @review: zkevin/2019年01月11日 14:51
 */
public abstract class AbstractClusterListener implements ClusterListener {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    public static final String PREFIX_ATALOG = "/suixingpay";
    public static final String BASE_CATALOG = PREFIX_ATALOG + "/datas";

    protected ClusterClient client;
    @Override
    public void setClient(ClusterClient client) {
        this.client = client;
    }
}
