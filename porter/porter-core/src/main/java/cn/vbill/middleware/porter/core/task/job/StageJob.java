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

package cn.vbill.middleware.porter.core.task.job;

import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;

import java.util.concurrent.ExecutionException;

/**
 * 阶段性工作
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:04
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:04
 */
public interface StageJob {

    /**
     * start接口方法
     *
     * @date 2018/8/9 上午9:49
     * @param: []
     * @return: void
     */
    void start() throws Exception;

    /**
     * canStart接口方法
     *
     * @date 2018/8/9 上午9:49
     * @param: []
     * @return: boolean
     */
    default boolean canStart() {
        return true;
    }

    /**
     * stop方法
     *
     * @date 2018/8/9 上午9:50
     * @param: []
     * @return: void
     */
    void stop();

    /**
     * output接口方法
     *
     * @date 2018/8/9 上午9:50
     * @param: []
     * @return: T
     */
    <T> T output() throws InterruptedException, TaskStopTriggerException;

    /**
     * 停止写入接口方法
     *
     * @date 2018/8/9 上午11:50
     * @param: []
     * @return: boolean
     */
    default boolean stopWaiting() {
        return false;
    }
    default boolean isPoolEmpty() {
        return true;
    }
    default boolean isPrevPoolEmpty()  {
        return true;
    }
}
