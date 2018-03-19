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
@Service("dbORACLESelectService")
public class DbOracleSelectService implements DbSelectService{

    @Override
    public Long pageTotal(String... sql) {
        return null;
    }

    @Override
    public List<String[]> list(String... sql) {
        return null;
    }

    @Override
    public List<String[]> page(Page<?> page, String... sql) {
        return null;
    }

    
}
