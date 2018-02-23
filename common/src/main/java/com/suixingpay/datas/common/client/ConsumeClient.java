/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 13:13
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client;

import com.suixingpay.datas.common.exception.ClientException;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * 消费源客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 13:13
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 13:13
 */
public interface ConsumeClient extends Client{
    <F, O> List<F> fetch(FetchCallback<F, O> callback);

    /**
     * 消费源客户端拆分
     * @param <T>
     * @return
     * @throws ClientException
     */
    default <T> List<T> split() throws ClientException {
        return Arrays.asList((T) this);
    }


    interface FetchCallback<F, O> {
        <F, O> F  accept(O o) throws ParseException;
    }

    String getSwimlaneId();
}
