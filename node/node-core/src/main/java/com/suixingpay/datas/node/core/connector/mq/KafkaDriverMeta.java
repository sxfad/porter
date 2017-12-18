package com.suixingpay.datas.node.core.connector.mq;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 11:24
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import com.suixingpay.datas.node.core.connector.DataDriverMeta;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月14日 11:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月14日 11:24
 */
public enum    KafkaDriverMeta  implements DataDriverMeta {
    INSTANCE();
    public  final String TOPIC="topic";
    public  final String GROUP="group";
    public  final String POLL_TIME_OUT="pollTimeOut";
    public  final String ONCE_POLL_SIZE="oncePollSize";
}
