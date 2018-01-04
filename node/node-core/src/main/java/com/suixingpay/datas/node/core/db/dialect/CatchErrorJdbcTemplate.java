/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月04日 18:06
 */
public class CatchErrorJdbcTemplate extends JdbcTemplate{
    public CatchErrorJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int update(String sql, Object... args) {
        try {
            return super.update(sql, args);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int update(String sql) {
        try {
            return super.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
