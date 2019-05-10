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

package cn.vbill.middleware.porter.common.plugin.config;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * 插件服务配置文件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月25日 10:28
 * @version: V1.0
 * @review: zkevin/2018年04月25日 10:28
 */
public interface PluginServiceConfig {
    /**
     * 用于插件服务客户端加载匹配
     * @param configName
     * @return
     */
    default boolean isMatch(String configName) {
        return getInstance().containsKey(StringUtils.trimToEmpty(configName));
    }

    Map<String, Class> getInstance();
}
