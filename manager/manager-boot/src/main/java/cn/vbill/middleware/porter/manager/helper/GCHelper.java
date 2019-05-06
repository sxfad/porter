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

package cn.vbill.middleware.porter.manager.helper;

import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * GC工具
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月11日 10:34
 * @version: V1.0
 * @review: zkevin/2019年03月11日 10:34
 */
public class GCHelper {
    /**
     * 内存定时清理线程
     */
    public static void run(Integer delayOfMinutes) {
        //最短5分钟执行一次
        delayOfMinutes = null == delayOfMinutes || delayOfMinutes < 5 ? 5 : delayOfMinutes;
        Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("instance-memory-gc"))
                .schedule(() -> System.gc(), delayOfMinutes, TimeUnit.MINUTES);
    }
}
