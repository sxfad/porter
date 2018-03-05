/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 15:43
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command.broadcast;


import com.suixingpay.datas.common.cluster.command.TaskPositionQueryCommand;
import com.suixingpay.datas.common.cluster.command.TaskPositionUploadCommand;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 15:43
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 15:43
 */
public interface TaskPosition {
    void upload(TaskPositionUploadCommand command) throws Exception;
    void query(TaskPositionQueryCommand command) throws Exception;
}
