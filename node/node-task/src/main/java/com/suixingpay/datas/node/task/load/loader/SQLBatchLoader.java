/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.load.loader;

import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import com.suixingpay.datas.node.core.event.ETLBucket;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月27日 16:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月27日 16:02
 */
public class SQLBatchLoader implements Loader {
    private final DbDialectFactory dbFactory = DbDialectFactory.INSTANCE;
    //需要在之前对datasource进行二次封装
    @Override
    public void load(ETLBucket bucket) {
        DbDialect dbDialect = dbFactory.getDbDialect(bucket.getDataSourceId());
     }
}
