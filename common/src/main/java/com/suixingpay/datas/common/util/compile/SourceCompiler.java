/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 13:34
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.util.compile;


import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.nio.charset.StandardCharsets;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 13:34
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 13:34
 */
public enum  SourceCompiler {
    INSTANCE();

    /**
     *
     * @param <T> 对象
     * @param source java源码
     * @param targetClass 目标class
     * @return
     * @throws Exception
     */
    public  <T> T newJavaObject(String source, Class targetClass) throws Exception {
        JavaSource javaSource = new JavaSource(source);
        Class<?> sourceClass = compile(javaSource);
        if (null != sourceClass && targetClass.isAssignableFrom(sourceClass)) {
            return (T) sourceClass.newInstance();
        }
        throw new UnexpectedException(sourceClass.getName() + "不是" + targetClass.getName() + "的子类或接口实现");
    }

    private Class<?> compile(JavaSource javaSource) throws IOException, ClassNotFoundException {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        //临时工作目录
        File tmpDir = new File(System.getProperty("java.io.tmpdir"));
        StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, Locale.ROOT, StandardCharsets.UTF_8);
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tmpDir));
        //java source Object
        SimpleJavaFileObject fileObject = new SimpleJavaFileObject(URI.create(javaSource.getClassName() + ".java"),
                JavaFileObject.Kind.SOURCE) {
            @Override
            public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
                return javaSource.getSource();
            }
        };

        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, fileManager, null,
                Collections.emptyList(), null, Arrays.asList(fileObject));
        if (task.call()) {
            URL classUrl = tmpDir.toURI().toURL();
            ClassLoader classLoader = new URLClassLoader(new URL[]{classUrl});
            return Class.forName(javaSource.getPackageName() + "." + javaSource.getClassName(), true, classLoader);
        }
        return null;
    }
}
