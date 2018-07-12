/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:20
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.core.loader;

import cn.vbill.middleware.porter.common.client.LoadClient;
import cn.vbill.middleware.porter.common.client.MetaQueryClient;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.config.DataLoaderConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.exception.DataLoaderBuildException;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
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
    private final List<DataLoader> LOADER_TEMPLATE = SpringFactoriesLoader.loadFactories(DataLoader.class, JavaFileCompiler.getInstance());

    public DataLoader getLoader(DataLoaderConfig config) throws ConfigParseException, ClientException, DataLoaderBuildException {
        Client client = AbstractClient.getClient(SourceConfig.getConfig(config.getSource()));
        //获取源数据查询配置
        DataLoader loader = newLoader(config.getLoaderName());
        if (!(client instanceof LoadClient)) {
            throw new ClientException(config.getSource() + "不是LoadClient实现");
        }
        if (!(client instanceof MetaQueryClient)) {
            throw new ClientException(config.getSource() + "MetaQueryClient");
        }
        loader.setLoadClient((LoadClient) client);
        loader.setMetaQueryClient((MetaQueryClient) client);
        //新增数据载入策略开关
        loader.setInsertOnUpdateError(config.isInsertOnUpdateError());
        return loader;
    }

    public DataLoader newLoader(String loaderName) throws DataLoaderBuildException {
        for (DataLoader t : LOADER_TEMPLATE) {
            if (t.isMatch(loaderName)) {
                try {
                    return t.getClass().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new DataLoaderBuildException(e.getMessage());
                }
            }
        }
        return null;
    }
}
