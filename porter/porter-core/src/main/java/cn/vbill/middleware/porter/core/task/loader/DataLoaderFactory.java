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

package cn.vbill.middleware.porter.core.task.loader;

import cn.vbill.middleware.porter.common.task.loader.LoadClient;
import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.exception.ConfigParseException;
import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.task.config.DataLoaderConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.exception.ClientException;
import cn.vbill.middleware.porter.common.task.exception.DataLoaderBuildException;
import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ClassUtils;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 10:20
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 10:20
 */
public enum DataLoaderFactory {

    /**
     * instance
     */
    INSTANCE();
    private final List<String> loaderTemplate = SpringFactoriesLoader.loadFactoryNames(DataLoader.class, JavaFileCompiler.getInstance());
    private static final Logger LOGGER = LoggerFactory.getLogger(DataLoaderFactory.class);

    /**
     * 获取Loader
     *
     * @date 2018/8/8 下午6:03
     * @param: [config]
     * @return: cn.vbill.middleware.porter.core.loader.DataLoader
     */
    public DataLoader getLoader(DataLoaderConfig config) throws ConfigParseException, ClientException, DataLoaderBuildException {
        //获取源数据查询配置
        DataLoader loader = newLoader(config.getLoaderName());
        Client client = AbstractClient.getClient(SourceConfig.getConfig(config.getSource(), loader.getDefaultClientType()));
        if (null == client) throw new ConfigParseException(config.getSource() + "不能识别的目标端链接信息");
        if (!(client instanceof LoadClient)) {
            throw new ClientException(config.getSource() + "无法识别为目标端链接客户端");
        }
        if (!(client instanceof MetaQueryClient)) {
            throw new ClientException(config.getSource() + "无法识别为目标端元数据客户端");
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
        for (String t : loaderTemplate) {
            try {
                Class<DataLoader> clazz = (Class<DataLoader>) ClassUtils.forName(t, DataLoader.class.getClassLoader());
                DataLoader tmpInstance = clazz.newInstance();
                if (tmpInstance.isMatch(loaderName)) {
                    return tmpInstance;
                }
            } catch (Throwable e) {
                LOGGER.warn("{}不匹配{}", t, loaderName, e);
            }
        }
        throw new DataLoaderBuildException();
    }
}
