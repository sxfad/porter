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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 14:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 14:47
 */

public class JavaClass  implements JavaFile {
    @Getter private final String filePath;
    @Getter private final String className;
    @Getter private final byte[] classData;

    public JavaClass(JavaFileConfig config) throws IOException {
        this.filePath = config.getContent();
        className = config.getClassName();
        classData = readClassData(filePath);
    }

    private byte[] readClassData(String filePath) throws IOException {
        URL myUrl = new File(filePath).toURI().toURL();
        URLConnection connection = myUrl.openConnection();
        InputStream input = connection.getInputStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int data = input.read();

        while (data != -1) {
            buffer.write(data);
            data = input.read();
        }
        input.close();

        return buffer.toByteArray();
    }
}
