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

package cn.vbill.middleware.porter.common.warning.config;

import java.util.Map;

import cn.vbill.middleware.porter.common.warning.entity.WarningPlugin;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:29
 */
public class WarningConfig {
    
    
    public WarningConfig() {
        
    }
    
    public WarningConfig(WarningPlugin strategy, WarningReceiver[] receiver, Map<String, String> client) {
        this.strategy = strategy;
        this.receiver = receiver;
        this.client = client;
    }

    // second
    private Integer frequencyOfSeconds = 60;

    private WarningPlugin strategy;

    private WarningReceiver[] receiver = new WarningReceiver[0];

    // 告警客户端
    private Map<String, String> client;

    public Integer getFrequencyOfSeconds() {
        return frequencyOfSeconds;
    }

    public void setFrequencyOfSeconds(Integer frequencyOfSeconds) {
        this.frequencyOfSeconds = frequencyOfSeconds;
    }

    public WarningPlugin getStrategy() {
        return strategy;
    }

    public void setStrategy(WarningPlugin strategy) {
        this.strategy = strategy;
    }

    public WarningReceiver[] getReceiver() {
        return receiver;
    }

    public void setReceiver(WarningReceiver[] receiver) {
        this.receiver = receiver;
    }

    public Map<String, String> getClient() {
        return client;
    }

    public void setClient(Map<String, String> client) {
        this.client = client;
    }
}
