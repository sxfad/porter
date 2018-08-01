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

package cn.vbill.middleware.porter.common.util.compile;

import cn.vbill.middleware.porter.common.config.JavaFileConfig;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 14:47
 */
public class JavaSource implements JavaFile {
    @Getter private final String packageName;
    @Getter private final String className;
    @Getter private final String simpleClassName;
    @Getter private final String source;
    public JavaSource(JavaFileConfig config) {
        this.source = config.getContent();
        this.className = config.getClassName();
        this.packageName = getPackage(config.getClassName());
        this.simpleClassName = getClassName(config.getClassName());
    }

    private static String getPackage(String className) {
        int index = className.lastIndexOf(".");
        return index > -1 ? className.substring(0, index) : StringUtils.EMPTY;
    }

    private static String getClassName(String className) {
        int index = className.lastIndexOf(".");
        return index > -1 ? className.substring(index + 1) : className;
    }
}
