/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 13:34
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.util.compile;


import com.suixingpay.datas.common.config.JavaFileConfig;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 13:34
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 13:34
 */
public class JavaFileCompilerTest {

    private static String RESOURCE_DIR;

    @BeforeClass
    public static void init() {
        RESOURCE_DIR = JavaFileCompilerTest.class.getResource("/").getPath().replace("out/test/classes", "src/test/resources");
    }

    @Test
    public void newJavaObject() throws Exception {
        JavaFileConfig config = new JavaFileConfig();
        config.setClassName("com.suixingpay.datas.common.util.compile.JavaFileSample");
        config.setContent("package com.suixingpay.datas.common.util.compile;"
                + "public class JavaFileSample {"
                + "    "
                + "    @Override"
                + "    public String toString() {"
                + "        return \"JavaFileSample\";"
                + "    }"
                + "}");

        Object obj = JavaFileCompiler.getInstance().newJavaObject(config, Object.class);
        Assert.assertNotNull(obj);
        Assert.assertNotNull(obj.toString());
        System.out.println(obj.toString());
    }

    @Test
    @Ignore
    public void newFromClassJavaObject() throws Exception {
        JavaFileConfig config = new JavaFileConfig();
        config.setClassName("com.suixingpay.datas.common.util.compile.JavaFileSample");
        config.setContent(RESOURCE_DIR + "JavaFileSample.class");
        Object obj = JavaFileCompiler.getInstance().newJavaObject(config, Object.class);
        Assert.assertNotNull(obj);
        Assert.assertNotNull(obj.toString());
        System.out.println(obj.toString());
    }

    @Test
    @Ignore
    public void newFromJarJavaObject() throws Exception {
        JavaFileConfig config = new JavaFileConfig();
        config.setClassName("com.suixingpay.datas.compiler.JarClass");
        config.setContent(RESOURCE_DIR + "/fromJarCompiler-1.0.jar");
        Object obj = JavaFileCompiler.getInstance().newJavaObject(config, Object.class);
        Assert.assertNotNull(obj);
        Assert.assertNotNull(obj.toString());
        System.out.println(obj.toString());
    }

    @Test
    public void loadPlugin() throws Exception {
        JavaFileCompiler.getInstance().loadPlugin(RESOURCE_DIR);
    }
}
