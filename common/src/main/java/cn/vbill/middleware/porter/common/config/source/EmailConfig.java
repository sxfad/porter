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

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 00:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 00:10
 */
public class EmailConfig extends SourceConfig {
    @Setter @Getter private String host;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private boolean smtpAuth = true;
    @Setter @Getter private boolean smtpStarttlsEnable = true;
    @Setter @Getter private boolean smtpStarttlsRequired = false;

    public EmailConfig() {
        sourceType = SourceType.EMAIL;
    }

    public EmailConfig(Map<String, String> properties) {
        this();
        super.setProperties(properties);
    }

    @Override
    protected void childStuff() {
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
