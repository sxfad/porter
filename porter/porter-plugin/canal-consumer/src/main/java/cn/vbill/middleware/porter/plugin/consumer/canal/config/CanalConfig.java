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

package cn.vbill.middleware.porter.plugin.consumer.canal.config;

import cn.vbill.middleware.porter.common.plugin.config.PluginServiceConfig;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.plugin.consumer.canal.CanalConsumerConst;
import cn.vbill.middleware.porter.plugin.consumer.canal.client.CanalClient;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class CanalConfig extends SourceConfig implements PluginServiceConfig {
    private static final String ADDRESS_SPLIT_CHARACTER = ":";

    //实例描述
    @Setter @Getter private Long canalId = System.nanoTime();
    @Setter @Getter private String canalName = UUID.randomUUID().toString();
    @Setter @Getter private CanalParameter.SourcingType sourcingType = CanalParameter.SourcingType.MYSQL;
    @Setter @Getter private Long slaveId = System.nanoTime();

    @Setter @Getter private String address;
    @Setter @Getter private String database;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private String filter = "";
    @Setter @Getter private int oncePollSize = 1000;
    @Setter @Getter private int pollTimeOut  = -1;
    @Setter @Getter private boolean parallel = false;

    @Override
    protected void childStuff() {
    }

    /**
     * getSocketAddress
     * @return
     */
    public InetSocketAddress getSocketAddress() {
        String[] addressArray = address.split(ADDRESS_SPLIT_CHARACTER);
        if (null != addressArray && addressArray.length == 2) {
            return new InetSocketAddress(addressArray[0], Integer.parseInt(addressArray[1]));
        }
        return null;
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }

    @Override
    protected boolean doCheck() {
        return !StringUtils.isBlank(address) && address.split(ADDRESS_SPLIT_CHARACTER).length == 2;
    }


    @Override
    public String getSwimlaneId() {
        return database;
    }

    @Override
    public Map<String, Class> getInstance() {
        return new HashMap<String, Class>() {
            {
                put(CanalConsumerConst.CONSUMER_SOURCE_TYPE_NAME.getCode(), CanalClient.class);
            }
        };
    }
}
