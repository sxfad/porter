/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.core.datasource.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import com.suixingpay.datas.common.datasource.AbstractSourceWrapper;
import com.suixingpay.datas.common.datasource.DataDriver;
import com.suixingpay.datas.common.datasource.DataDriverType;
import com.suixingpay.datas.common.datasource.meta.JDBCDriverMeta;
import org.apache.commons.lang3.StringUtils;
import javax.sql.DataSource;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 14:10
 */
public class JDBCSourceWrapper extends AbstractSourceWrapper {
    private DruidDataSource dataSource;
    private final Long maxWait;
    public JDBCSourceWrapper(DataDriver driver) {
        super(driver);
        JDBCDriverMeta meta = (JDBCDriverMeta) driver.getType().getMeta();
        String maxWaitStr = driver.getExtendAttr().getOrDefault(meta.MAX_WAIT,"10000");
        //默认driver class name
        if (StringUtils.isBlank(driver.getDriverClassName())) {
            if (driver.getType() == DataDriverType.MYSQL) {
                driver.setDriverClassName("com.mysql.jdbc.Driver");
            } else if (driver.getType() == DataDriverType.ORACLE) {
                driver.setDriverClassName("oracle.jdbc.driver.OracleDriver");
            }
        }
        maxWait = Long.parseLong(maxWaitStr);
    }

    @Override
    protected void doDestroy() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    @Override
    protected void doCreate() {
        dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driver.getDriverClassName());
        dataSource.setUrl(driver.getUrl());
        dataSource.setUsername(driver.getUserName());
        dataSource.setPassword(driver.getPassword());
        dataSource.setMaxWait(maxWait);
        if (driver.getType().getValue() == DataDriverType.MYSQL_VALUE) {
            dataSource.setValidationQuery("select 1");
        } else if (driver.getType().getValue() == DataDriverType.ORACLE_VALUE) {
            dataSource.setValidationQuery("select 1 from dual");
            dataSource.addConnectionProperty("restrictGetTables", "true");
            dataSource.addConnectionProperty("zeroDateTimeBehavior", "convertToNull");// 将0000-00-00的时间类型返回null
            dataSource.addConnectionProperty("yearIsDateType", "false");// 直接返回字符串，不做year转换date处理
        }
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
