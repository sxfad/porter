/**
 *
 */
package com.suixingpay.datas.manager.service;

import java.util.List;
import java.util.Map;

import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public interface DbSelectService {

    List<String> list(JDBCVo jvo, String sql, Map<String, Object> map);

    Long pageTotal(JDBCVo jvo, String sql, String prefix, String tableName);

    List<Object> page(JDBCVo jvo, Page<Object> page, String sql, String prefix, String tableName);
}
