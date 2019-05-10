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

package cn.vbill.middleware.porter.common.task.loader;

import cn.vbill.middleware.porter.common.task.consumer.MetaQueryClient;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.common.util.db.SqlTemplate;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 15:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 15:46
 */
public interface LoadClient extends MetaQueryClient {
    /**
     * 公共数据源重置
     */
    default void reset() {

    }
    default int update(String type, String sql, Object... args) throws TaskStopTriggerException, InterruptedException {
        throw new UnsupportedOperationException();
    }
    default int[] batchUpdate(String sqlType, String sql, List<Object[]> batchArgs) throws TaskStopTriggerException, InterruptedException {
        throw new UnsupportedOperationException();
    }

    default SqlTemplate getSqlTemplate() {
        throw new UnsupportedOperationException();
    }
}
