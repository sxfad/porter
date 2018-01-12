/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 15:59
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.task.transform.transformer;

import com.suixingpay.datas.common.db.TableMapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.event.etl.ETLBucket;
import com.suixingpay.datas.node.task.worker.TaskWork;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月26日 15:59
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月26日 15:59
 */
public interface Transformer {
    int order();
    void transform(ETLBucket bucket, TaskWork work, DbDialect targetDialect);
}
