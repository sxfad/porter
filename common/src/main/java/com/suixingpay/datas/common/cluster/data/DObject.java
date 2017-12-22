package com.suixingpay.datas.common.cluster.data;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 17:42
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.alibaba.fastjson.JSON;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 17:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 17:42
 */
public  abstract class DObject {
    public String toString() {
        return JSON.toJSONString(this);
    }
    public static <T> T  fromString(String string, Class<T> clazz) {
        return JSON.parseObject(string, clazz);
    }
    public abstract <T> void merge(T data);

    public  <T> T snapshot(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this), clazz);
    }
}
