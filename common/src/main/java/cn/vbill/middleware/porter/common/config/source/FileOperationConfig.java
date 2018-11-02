/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:33
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config.source;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:33
 * @version: V1.0
 * @review: zkevin/2018年10月19日 14:33
 */
public class FileOperationConfig extends SourceConfig {
    @Setter @Getter private String home = System.getProperty("user.home") + "/.porter";
    public FileOperationConfig() {
        sourceType = SourceType.FILE;
    }

    public FileOperationConfig(Map<String, String> properties) {
        this();
        super.setProperties(properties);
    }

    @Override
    protected void childStuff() {

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
