/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 15:16
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.exception.ClientException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公共客户端资源容器
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 10:37
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 10:37
 */
public enum PublicClientContext {
    INSTANCE();
    private final Map<String,Client> allSources = new ConcurrentHashMap<String, Client>();

    /**
     * 初始化公用DataSource
     * @param configs
     */
    public void initialize(List<Pair<String, SourceConfig>> configs) throws ClientException, IOException {
        for (Pair<String, SourceConfig> p : configs) {
            Client client = AbstractClient.getClient(p.getRight());
            client.setPublic(true);
            client.start();
            allSources.put(p.getLeft(), client);
        }
    }

    public Client getSource(String sourceName) {
        return allSources.getOrDefault(sourceName, null);
    }
}
