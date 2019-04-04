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

package cn.vbill.middleware.porter.task.alert.alerter;

import cn.vbill.middleware.porter.common.task.statistics.DTaskStat;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import org.apache.commons.lang3.tuple.Triple;

/**
 * 告警接口
 */
public interface Alerter {

    /**
     * check
     *
     * @date 2018/8/9 下午2:02
     * @param: [consumer, loader, stat, checkMeta, receivers]
     * @return: void
     */
    void check(DataConsumer consumer, DataLoader loader, DTaskStat stat, Triple<String[], String[], String[]> checkMeta) throws InterruptedException;
}
