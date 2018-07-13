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

package cn.vbill.middleware.porter.common.config.source;

import cn.vbill.middleware.porter.common.dic.SourceType;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kudu配置
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class KuduConfig extends SourceConfig {
    private static final String SERVER_SPLIT_CHARACTER = ",";
    //ip:port
    @Setter @Getter private List<String> servers = new ArrayList<>();
    @Setter @Getter private int workerCount  = 10;

    public KuduConfig() {
        sourceType = SourceType.KUDU;
    }

    @Override
    protected void childStuff() {
        String serversStr = getProperties().getOrDefault("servers", "");
        if (!StringUtils.isBlank(serversStr)) {
            servers.addAll(Arrays.stream(serversStr.split(SERVER_SPLIT_CHARACTER)).collect(Collectors.toList()));
        }
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"servers"};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
