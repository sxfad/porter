/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:20
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.loader;

import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.DataLoaderConfig;
import com.suixingpay.datas.common.config.source.SourceConfig;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.node.core.source.PublicSourceFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 10:20
 */
public enum DataLoaderFactory {
    INSTANCE();
    private final List<DataLoader> LOADER_TEMPLATE = SpringFactoriesLoader.loadFactories(DataLoader.class, null);

    public DataLoader getLoader(DataLoaderConfig config) throws ClientException, InstantiationException, IllegalAccessException {
        Client client = null;
        //获取源数据查询配置
        Config sourceConfig = Config.getConfig(config.getSource());
        //如果是公共资源就从公共资源池获取
        if (sourceConfig instanceof SourceConfig && ((SourceConfig)sourceConfig).isPublic()) {
            client = PublicSourceFactory.INSTANCE.getSource(((SourceConfig)sourceConfig).getSourceName());
        } else {
            client = AbstractClient.getClient(sourceConfig);
        }

        DataLoader loader = newLoader(config.getLoaderName());
        loader.setClient(client);
        return loader;
    }

    public DataLoader newLoader(String loaderName) throws IllegalAccessException, InstantiationException {
        for (DataLoader t : LOADER_TEMPLATE) {
            if (t.isMatch(loaderName)) {
                return t.getClass().newInstance();
            }
        }
        return null;
    }
}
