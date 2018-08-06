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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 13:34
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 13:34
 */
public class JavaFileCompiler extends URLClassLoader {
    private static final String PLUGIN_HOME = System.getProperty("app.home") + "/plugins";
    private final AtomicBoolean isLoadPlugin = new AtomicBoolean(false);
    private static final JavaFileCompiler COMPILER = new JavaFileCompiler(new URL[0]);

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaFileCompiler.class);

    public JavaFileCompiler(URL[] urls) {
        super(urls);
    }

    public static JavaFileCompiler getInstance() {
        return COMPILER;
    }

    /**
     * @param <T>         对象
     * @param source      java源码
     * @param targetClass 目标class
     * @return
     * @throws Exception
     */
    public <T> T newJavaObject(JavaFileConfig source, Class targetClass) throws Exception {
        Class<?> sourceClass = null;
        JavaFile javaFile = null;
        //class
        if (source.getContent().endsWith(".class")) {
            javaFile = new JavaClass(source);
        } else if (source.getContent().endsWith(".jar")) {
            javaFile = new JavaJarClass(source);
        } else if (source.getContent().endsWith(".java")) {
            //读取源码文件,转换为JavaSource对象
            source.setContent(readSource(source.getContent()));
            javaFile = new JavaSource(source);
        } else { //source
            javaFile = new JavaSource(source);
        }
        sourceClass = compile(javaFile);
        if (null != sourceClass && targetClass.isAssignableFrom(sourceClass)) {
            return (T) sourceClass.newInstance();
        }
        throw new UnexpectedException(sourceClass + "不是" + targetClass + "的子类或接口实现");
    }

    private Class<?> compile(JavaFile javaFile) throws IOException, ClassNotFoundException {
        //判断当前类是否已被jvm加载
        try {
            Class<?> findExistsClass = Class.forName(javaFile.getClassName(), true, this);
            if (null != findExistsClass) {
                return findExistsClass;
            }
        } catch (Throwable e) {
            LOGGER.error("%s", e);
        }
        //java class file
        if (null != javaFile && javaFile instanceof JavaClass) {
            JavaClass javaClass = (JavaClass) javaFile;
            if (null != javaClass.getClassData() && javaClass.getClassData().length > 0) {
                return defineClass(javaClass.getClassName(), javaClass.getClassData(), 0, javaClass.getClassData().length);
            }
        } else if (null != javaFile && javaFile instanceof JavaJarClass) {
            JavaJarClass javaClass = (JavaJarClass) javaFile;
            this.addURL(javaClass.getJarFile());
            return this.loadClass(javaClass.getClassName());
        } else if (null != javaFile && javaFile instanceof JavaSource) {
            JavaSource javaSource = (JavaSource) javaFile;
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            //临时工作目录
            File tmpDir = new File(System.getProperty("java.io.tmpdir"));
            StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager(null, Locale.ROOT, StandardCharsets.UTF_8);
            fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(tmpDir));
            //java source Object
            SimpleJavaFileObject fileObject = new SimpleJavaFileObject(URI.create(javaSource.getSimpleClassName() + ".java"),
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
                this.addURL(classUrl);
                //ClassLoader classLoader = new URLClassLoader(new URL[]{classUrl});
                return Class.forName(javaSource.getClassName(), true, this);
            }
        }
        return null;
    }

    public void loadPlugin() throws MalformedURLException {
        loadPlugin(PLUGIN_HOME);
    }

    public void loadPlugin(String filePath) throws MalformedURLException {
        if (isLoadPlugin.compareAndSet(false, true)) {
            File[] jars = new File(filePath).listFiles(pathname -> pathname.getName().endsWith(".jar"));
            if (null != jars) {
                for (File jar : jars) {
                    this.addURL(jar.toURI().toURL());
                }
            }
        }

    }


    private String readSource(String sourceFile) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFile))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }
}
