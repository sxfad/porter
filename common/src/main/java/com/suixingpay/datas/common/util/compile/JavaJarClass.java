/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.util.compile;

import com.suixingpay.datas.common.config.JavaFileConfig;
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
