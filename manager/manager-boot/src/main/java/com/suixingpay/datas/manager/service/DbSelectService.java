/**
 * 
 */
package com.suixingpay.datas.manager.service;

import java.util.List;

import com.suixingpay.datas.manager.web.page.Page;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public interface DbSelectService {

    Long pageTotal(String ... sql);

    List<String[]> list(String ... sql);

    List<String[]> page(Page<?> page,String ... sql);
}
