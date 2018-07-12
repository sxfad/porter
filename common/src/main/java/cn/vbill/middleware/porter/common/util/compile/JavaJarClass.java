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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 14:47
 */

public class JavaJarClass implements JavaFile {
    @Getter private final URL jarFile;
    @Getter private final String className;

    public JavaJarClass(JavaFileConfig config) throws MalformedURLException {
        this.className = config.getClassName();
        this.jarFile = new File(config.getContent()).toURI().toURL();
    }
}
