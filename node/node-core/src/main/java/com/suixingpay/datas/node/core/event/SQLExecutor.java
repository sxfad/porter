package com.suixingpay.datas.node.core.event;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:44
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import org.apache.commons.lang3.tuple.Triple;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 14:44
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 14:44
 */
public interface SQLExecutor {
    int update(String updateSQL) throws SQLException;
    List<Map<String, Triple<String, Class, Object>>> query(String querySQL) throws SQLException;
}
