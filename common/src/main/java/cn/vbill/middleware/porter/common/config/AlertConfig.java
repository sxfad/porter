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

package cn.vbill.middleware.porter.common.config;

import java.util.Map;

import cn.vbill.middleware.porter.common.dic.AlertPlugin;
import cn.vbill.middleware.porter.common.alert.AlertReceiver;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 10:29
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 10:29
 */
public class AlertConfig {
    
    
    public AlertConfig() {
        
    }
    
    public AlertConfig(AlertPlugin strategy, AlertReceiver[] receiver, Map<String, String> client) {
        this.strategy = strategy;
        this.receiver = receiver;
        this.client = client;
    }

    // second
    @Getter
    @Setter
    private Integer frequencyOfSeconds = 60;

    @Getter
    @Setter
    private AlertPlugin strategy;

    @Getter
    @Setter
    private AlertReceiver[] receiver = new AlertReceiver[0];

    // 告警客户端
    @Getter
    @Setter
    private Map<String, String> client;
}
