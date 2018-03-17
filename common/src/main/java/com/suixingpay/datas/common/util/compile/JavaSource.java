/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.util.compile;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 14:47
 */
public class JavaSource {
    @Getter private final String packageName;
    @Getter private final String className;
    @Getter private final String source;
    public JavaSource(String source) throws MalformedPatternException {
        this.source = source;
        this.packageName = getPackage(source);
        this.className = getClassName(source);
    }

    private static String getPackage(String source) throws MalformedPatternException {
        return find(source, "package (?s).*?;")
                .replaceAll("package ", StringUtils.EMPTY)
                .replaceAll(";", StringUtils.EMPTY)
                .trim();
    }

    private static String getClassName(String source) throws MalformedPatternException {
        return find(source, "public class (?s).*?{")
                .split("extends")[0]
                .split("implements")[0]
                .replaceAll("public class ", StringUtils.EMPTY)
                .replace("{", StringUtils.EMPTY)
                .trim();
    }

    private static String find(String text, String regex) throws MalformedPatternException {

        if (StringUtils.isBlank(text)) return StringUtils.EMPTY;

        PatternMatcher textMatcher = new Perl5Matcher();
        if (textMatcher.contains(text, new Perl5Compiler().compile(regex,
                Perl5Compiler.CASE_INSENSITIVE_MASK | Perl5Compiler.READ_ONLY_MASK))) {
            return StringUtils.trimToEmpty(textMatcher.getMatch().group(0));
        }
        return StringUtils.EMPTY;
    }
}
