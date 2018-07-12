/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config.source;

import cn.vbill.middleware.porter.common.dic.DbType;
import cn.vbill.middleware.porter.common.dic.SourceType;
import cn.vbill.middleware.porter.common.config.SourceConfig;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class JDBCConfig extends SourceConfig {
    //驱动类型
    @Setter @Getter private String driverClassName;
    @Setter @Getter private String url;
    @Setter @Getter private String userName;
    @Setter @Getter private String password;
    @Setter @Getter private int maxWait = 10000;
    @Setter @Getter private int minPoolSize = 20;
    @Setter @Getter private int maxPoolSize = 20;
    @Setter @Getter private int initialPoolSize = 5;
    @Setter @Getter private int connectionErrorRetryAttempts = 1;
    @Setter @Getter private int validationQueryTimeout = 3000;
    @Setter @Getter private DbType dbType;
    @Setter @Getter private boolean makePrimaryKeyWhenNo = true;

    @Setter @Getter private boolean testOnReturn = true;
    @Setter @Getter private boolean testOnBorrow = true;

    public  JDBCConfig() {
        sourceType =  SourceType.JDBC;
    }

    @Override
    protected void childStuff() {
        String typeStr = getProperties().getOrDefault("dbType", "");
        DbType enumType = !StringUtils.isBlank(typeStr) ? DbType.valueOf(typeStr) : null;
        if (null != enumType) {
            dbType = enumType;
        }

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"dbType"};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
