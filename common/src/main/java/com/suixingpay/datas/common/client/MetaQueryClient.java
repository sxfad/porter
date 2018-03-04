/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 14:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.db.meta.TableSchema;

import java.util.Date;

/**
 * 消费源源数据查询客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 14:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 14:42
 */
public interface MetaQueryClient extends Client {
    TableSchema getTable(String schema, String tableName) throws Exception;
    int getDataCount(String schema, String table, String updateDateColumn, Date startTime, Date endTime);
}
