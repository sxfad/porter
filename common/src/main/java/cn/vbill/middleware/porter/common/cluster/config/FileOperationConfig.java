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

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:33
 * @version: V1.0
 * @review: zkevin/2018年10月19日 14:33
 */
public class FileOperationConfig extends SourceConfig {
    @Setter @Getter private String home = System.getProperty("user.home") + "/.porter";
    public FileOperationConfig() {
        sourceType = SourceType.FILE;
    }

    public FileOperationConfig(Map<String, String> properties) {
        this();
        super.setProperties(properties);
    }

    @Override
    protected void childStuff() {

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
