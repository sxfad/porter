/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 15:18
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command.broadcast;

import com.suixingpay.datas.common.cluster.command.NodeRegisterCommand;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 15:18
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 15:18
 */
public interface NodeRegister {
    void nodeRegister(NodeRegisterCommand command) throws Exception;
}
