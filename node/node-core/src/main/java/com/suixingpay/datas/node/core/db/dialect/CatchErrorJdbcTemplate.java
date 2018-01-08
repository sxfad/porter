/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core.db.dialect;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月04日 18:06
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月04日 18:06
 */
public class CatchErrorJdbcTemplate extends JdbcTemplate{
    private static final Logger LOGGER = LoggerFactory.getLogger(CatchErrorJdbcTemplate.class);
    public CatchErrorJdbcTemplate(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public int update(String sql, Object... args) {
        int affect = 0;
        try {
            affect = super.update(sql, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("sql:{},params:{},affect:{}", sql, JSON.toJSONString(Arrays.asList(args)), affect);
        return  affect;
    }

    @Override
    public int update(String sql) {
        int affect = 0;
        try {
            affect = super.update(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.debug("sql:{},affect:{}", sql, affect);
        return affect;
    }
}
