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
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月19日 16:18
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月19日 16:18
 */
public class JavaFileConfig {

    public JavaFileConfig() {

    }

    public JavaFileConfig(String className, String content) {
        this.className = className;
        this.content = content;
    }

    // package.className
    @Getter
    @Setter
    private String className;
    // 文件路径或源码内容
    @Getter
    @Setter
    private String content;
}
