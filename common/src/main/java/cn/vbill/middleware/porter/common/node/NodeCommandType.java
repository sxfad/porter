/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:48
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.node;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 17:48
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 17:48
 */
public enum  NodeCommandType {
    //停止|接收任务推送
    CHANGE_STATUS,
    //停止当前任务 NodeStatusType-SUSPEND
    RELEASE_WORK,
    //节点最大工作容量
    WORK_LIMIT;
}
