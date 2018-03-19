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
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;

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
