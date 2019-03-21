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

package cn.vbill.middleware.porter.plugin.loader.jdbc.config;

import cn.vbill.middleware.porter.common.config.PluginServiceConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.DbType;
import cn.vbill.middleware.porter.plugin.loader.jdbc.JdbcLoaderConst;
import cn.vbill.middleware.porter.plugin.loader.jdbc.client.JDBCClient;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class JDBCConfig extends SourceConfig implements PluginServiceConfig {
    //驱动类型
    @Setter @Getter private String driverClassName;
    @Setter @Getter private String url;
    @Setter @Getter private String userName;
    @Setter @Getter private String password;
    @Setter @Getter private int maxWait = 10000;
    @Setter @Getter private int minPoolSize = 20;
    @Setter @Getter private int maxPoolSize = 20;
    @Setter @Getter private int initialPoolSize = 5;
    @Setter @Getter private int connectionErrorRetryAttempts = 1;
    @Setter @Getter private int validationQueryTimeout = 3000;
    @Setter @Getter private DbType dbType;
    @Setter @Getter private boolean makePrimaryKeyWhenNo = true;

    @Setter @Getter private boolean testOnReturn = true;
    @Setter @Getter private boolean testOnBorrow = true;

    //sql异常重试次数5*1 min
    @Setter @Getter private int retries = 5;

    @Override
    protected void childStuff() {
        String typeStr = getProperties().getOrDefault("dbType", "");
        DbType enumType = !StringUtils.isBlank(typeStr) ? DbType.valueOf(typeStr) : null;
        if (null != enumType) {
            dbType = enumType;
        }

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"dbType"};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }

    @Override
    public String getInstanceClientType() {
        return JDBCClient.class.getName();
    }


    @Override
    public String getInstanceConfigType() {
        return JdbcLoaderConst.LOADER_SOURCE_TYPE_NAME.getCode();
    }
}
