/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:23
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.datasource;

import javax.sql.DataSource;
import java.util.UUID;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:23
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:23
 */
public interface DataSourceWrapper {
    boolean isPrivatePool();
    void setPrivatePool(boolean isPrivate);
    DataSource getDataSource();
    void destroy();
    void create();

    /**
     * 后期从配置中心读取
     * @return
     */
    String getUniqueId();
}
