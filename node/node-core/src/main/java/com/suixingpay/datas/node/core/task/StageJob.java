package com.suixingpay.datas.node.core.task;/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import java.io.IOException;

/**
 * 阶段性工作
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:04
 */
public interface StageJob {
    void start() throws IOException;
    default boolean canStart() {
        return true;
    }
    void stop();
    <T> T output() throws Exception;
    default boolean stopWaiting() {
        return true;
    }
    default boolean isPoolEmpty() {return true;}
    default boolean isPrevPoolEmpty()  {return true;}
}
