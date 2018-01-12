package com.suixingpay.datas.node.task.alert.alerter;

import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.task.worker.TaskWork;
import org.apache.commons.lang3.tuple.Triple;

import java.util.List;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

public interface Alerter {
    void check(DbDialect sourceDialect, DbDialect targetDialect, DTaskStat stat, Triple<String[], String[], String[]> checkMeta);
}
