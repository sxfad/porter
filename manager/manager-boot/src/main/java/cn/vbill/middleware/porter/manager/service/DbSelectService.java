/**
 *
 */
package cn.vbill.middleware.porter.manager.service;

import cn.vbill.middleware.porter.manager.core.dto.JDBCVo;
import cn.vbill.middleware.porter.manager.core.entity.DataSource;
import cn.vbill.middleware.porter.manager.web.page.Page;

import java.util.List;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public interface DbSelectService {

    List<String> list(DataSource dataSource, JDBCVo jvo, String sql, Map<String, Object> map);

    Long pageTotal(DataSource dataSource, JDBCVo jvo, String sql, String prefix, String tableName);

    List<Object> page(DataSource dataSource, JDBCVo jvo, Page<Object> page, String sql, String prefix, String tableName);

    List<String> fieldList(DataSource dataSource, JDBCVo jvo, String sql, String tableAllName);
}
