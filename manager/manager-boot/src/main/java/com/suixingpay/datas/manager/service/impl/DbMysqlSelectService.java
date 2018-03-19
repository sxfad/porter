/**
 * 
 */
package com.suixingpay.datas.manager.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.suixingpay.datas.manager.service.DbSelectService;
import com.suixingpay.datas.manager.web.page.Page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
@Service("dbMYSQLSelectService")
public class DbMysqlSelectService implements DbSelectService {

    /* (non-Javadoc)
     * @see com.suixingpay.datas.manager.service.DbSelectService#pageTotal(java.lang.String[])
     */
    @Override
    public Long pageTotal(String... sql) {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see com.suixingpay.datas.manager.service.DbSelectService#list(java.lang.String[])
     */
    @Override
    public List<String[]> list(String... sql) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<String[]> page(Page<?> page, String... sql) {
        // TODO Auto-generated method stub
        return null;
    }

}
