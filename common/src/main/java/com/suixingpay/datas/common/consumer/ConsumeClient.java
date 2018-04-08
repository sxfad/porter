/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 13:13
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.consumer;

import com.suixingpay.datas.common.client.Client;
import com.suixingpay.datas.common.exception.ClientException;
import com.suixingpay.datas.common.exception.ConfigParseException;
import com.suixingpay.datas.common.exception.TaskStopTriggerException;

import java.util.Arrays;
import java.util.List;

/**
 * 消费源客户端
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 13:13
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 13:13
 */
public interface ConsumeClient extends Client {
    /**
     * 是否自动提交消费同步点
     * @return
     */
    boolean isAutoCommitPosition();

    /**
     * 提交消费同步点，只有是手动提交时才会更新消费器客户端
     * @param position
     * @throws TaskStopTriggerException
     */
    void commitPosition(Position position) throws TaskStopTriggerException;

    /**
     * 初始化消费同步点，只有是手动提交时才会更新消费器客户端
     *
     * @param taskId
     * @param swimlaneId
     *@param position  @throws TaskStopTriggerException
     */
    void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException;

    /**
     * 获取消费泳道编号
     * @return
     */
    String getSwimlaneId();

    /**
     * 根据消费源客户端配置，拆分消费泳道
     * @param <T>
     * @return
     * @throws ClientException
     */
    default <T> List<T> splitSwimlanes() throws ClientException, ConfigParseException {
        return Arrays.asList((T) this);
    }

    /**
     * 提取数据
     * @param callback
     * @param <F>  同步中间件统一对象模型
     * @param <O>  消费客户端数据结构
     * @return 同步中间件统一对象模型列表
     */
    <F, O> List<F> fetch(FetchCallback<F, O> callback) throws TaskStopTriggerException, InterruptedException;

    /**
     * 回调函数
     * @param <F>
     * @param <O>
     */
    interface FetchCallback<F, O> {
        default <F, O> F  accept(O o) {
            return null;
        }
        default <F, O> List<F>  acceptAll(O o) throws Exception {
            return null;
        }
    }


}
