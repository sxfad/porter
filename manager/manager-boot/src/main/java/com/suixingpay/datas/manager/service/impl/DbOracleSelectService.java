/**
 * 
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.core.dto.JDBCVo;
import com.suixingpay.datas.manager.service.DbSelectService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Service("dbORACLESelectService")
public class DbOracleSelectService implements DbSelectService{

    @Override
    public List<String> list(JDBCVo jvo, String sql, Map<String, Object> map) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long pageTotal(JDBCVo jvo, String sql, String prefix, String tableName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Object> page(JDBCVo jvo, Page<Object> page, String sql, String prefix,String tableName) {
        String executeSql = "select * from (select t.*,rownum from ("+sql+") as t where 1=1) where rownum>? and  rownum<=?";
        
        return null;
    }
}
