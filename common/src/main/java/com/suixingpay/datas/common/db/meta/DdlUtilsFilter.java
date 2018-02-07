/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:20
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.db.meta;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public abstract class DdlUtilsFilter {

    /**
     * 返回要获取 {@linkplain DatabaseMetaData} 的 {@linkplain Connection}，不能返回null
     * 
     * @param con
     * @return
     */
    public Connection filterConnection(Connection con) throws Exception {
        return con;
    }

    /**
     * 对 databaseMetaData 做一些过滤,返回 {@linkplain DatabaseMetaData}，不能为 null
     * 
     * @param databaseMetaData
     * @return
     */
    public DatabaseMetaData filterDataBaseMetaData(JdbcTemplate jdbcTemplate, Connection con,
                                                   DatabaseMetaData databaseMetaData) throws Exception {
        return databaseMetaData;
    }

}
