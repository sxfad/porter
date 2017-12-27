/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:13
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.task;

/**
 * 一期任务发布采用配置文件形式，后期增加管理节点配置实现
 * 没采用管理节点轮询实现,基于ZOOKEEPER监听实现
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:13
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 17:13
 */
public interface TaskEventListener {
    void onEvent(TaskEvent event);
}
