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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月03日 16:44
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月03日 16:44
 */
@NoArgsConstructor
public class DataLoaderConfig {
    public DataLoaderConfig(String loaderName, Map<String, String> source) {
        this.loaderName = loaderName;
        this.source = source;
    }

    public DataLoaderConfig(String loaderName, Map<String, String> source, Boolean isUsing,
            boolean insertOnUpdateError) {
        this.loaderName = loaderName;
        this.source = source;
        this.isUsing = isUsing;
        this.insertOnUpdateError = insertOnUpdateError;
    }

    // 目标插件
    @Getter
    @Setter
    private String loaderName;
    @Getter
    @Setter
    private Map<String, String> source;

    // 是否启用数据源
    @Getter
    @Setter
    private Boolean isUsing = true;

    // 新增更新转插入策略开关
    @Getter
    @Setter
    private boolean insertOnUpdateError = true;
}
