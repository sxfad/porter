/**
 * 
 */
package com.suixingpay.datas.manager.service;

import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public interface DictService {

    Map<String, Map<String, Object>> dictAll();
    
    Map<String, Object> dictByType(String type);
}
