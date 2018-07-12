/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 14:51
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.core.event.s;

import cn.vbill.middleware.porter.common.util.compile.JavaFileCompiler;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月05日 14:51
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月05日 14:51
 */
public enum  ConverterFactory {
    INSTANCE();

    private final List<EventConverter> CONVERTERS;

    ConverterFactory() {
        /**
         * 自定义Converter插件由JavaFileCompiler载入，故需要JavaFileCompiler作为ClassLoader
         */
        CONVERTERS = SpringFactoriesLoader.loadFactories(EventConverter.class, JavaFileCompiler.getInstance());
    }

    public  EventConverter getConverter(String name) {
        for (EventConverter converter : CONVERTERS) {
            if (converter.getName().equals(name)) {
                return converter;
            }
        }
        return null;
    }
}
