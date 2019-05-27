/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.common.task.consumer;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.client.Client;

import java.util.List;

/**
 * 消费源客户端
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 13:13
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 13:13
 */
public interface ConsumeClient extends Client {
    /**
     * 是否自动提交消费同步点
     *
     * @return
     */
    boolean isAutoCommitPosition();

    /**
     * 提交消费同步点，只有是手动提交时才会更新消费器客户端
     *
     * @param position
     * @return 与生产进度差额
     * @throws TaskStopTriggerException
     */
    long commitPosition(Position position) throws TaskStopTriggerException;

    /**
     * 初始化消费同步点，只有是手动提交时才会更新消费器客户端
     *
     * @param taskId
     * @param swimlaneId
     * @param position   @throws TaskStopTriggerException
     */
    void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException;

    /**
     * 获取消费泳道编号
     *
     * @return
     */
    String getSwimlaneId();

    /**
     * 提取数据
     *
     * @param callback
     * @param <F>      同步中间件统一对象模型
     * @param <O>      消费客户端数据结构
     * @return 同步中间件统一对象模型列表
     */
    <F, O> List<F> fetch(FetchCallback<F, O> callback) throws TaskStopTriggerException, InterruptedException;

    /**
     * 回调函数
     *
     * @param <F>
     * @param <O>
     */
    interface FetchCallback<F, O> {
        /**
         * accept
         *
         * @param o
         * @param <F>
         * @param <O>
         * @return
         */
        default <F, O> F accept(O o) {
            return null;
        }

        /**
         * acceptAll
         *
         * @param o
         * @param <F>
         * @param <O>
         * @return
         * @throws Exception
         */
        default <F, O> List<F> acceptAll(O o) throws TaskStopTriggerException {
            return null;
        }
    }


    String getInitiatePosition(String offset) throws TaskStopTriggerException;
}
