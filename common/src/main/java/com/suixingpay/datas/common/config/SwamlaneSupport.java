/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月29日 15:09
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;


import com.suixingpay.datas.common.exception.ConfigParseException;

import java.util.List;

/**
 * 对于可消费数据源，实现泳道拆分，最大限度减少任务消费端数据集
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月29日 15:09
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月29日 15:09
 */
public interface SwamlaneSupport {
    <T extends SourceConfig> List<T> swamlanes() throws ConfigParseException;
    default String getSwimlaneId() {
        throw new UnsupportedOperationException("不支持的方法调用");
    }
}
