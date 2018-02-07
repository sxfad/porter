/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:08
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 14:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 14:08
 */
public interface ClusterClient<S> {

    List<String> getChildren(String path);

    Pair<String, S> getData(String path);

    String  create(String path, boolean isTemp, String data)  throws Exception;

    S setData(String path, String data, int version)  throws Exception;

    S exists(String path, boolean watch)  throws Exception;

    void delete(String path) throws Exception;
}
