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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoaderFactory.class);

    /**
     * 获取Loader
     *
     * @date 2018/8/8 下午6:03
     * @param: [config]
     * @return: cn.vbill.middleware.porter.core.loader.DataLoader
     */
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

    /**
     * newLoader
     *
     * @date 2018/8/8 下午6:03
     * @param: [loaderName]
     * @return: cn.vbill.middleware.porter.core.loader.DataLoader
     */
    public DataLoader newLoader(String loaderName) throws DataLoaderBuildException {
        for (DataLoader t : LOADER_TEMPLATE) {
            if (t.isMatch(loaderName)) {
                try {
                    return t.getClass().newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.error("%s", e);
                    throw new DataLoaderBuildException(e.getMessage());
                }
            }
        }
        return null;
    }
}
