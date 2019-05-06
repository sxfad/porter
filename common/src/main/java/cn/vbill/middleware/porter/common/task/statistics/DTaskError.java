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

package cn.vbill.middleware.porter.common.task.statistics;

import cn.vbill.middleware.porter.common.statistics.DObject;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 任务资源锁定
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */
@NoArgsConstructor
public class DTaskError extends DObject {
    @Setter @Getter private String taskId;
    @Setter @Getter private String swimlaneId;
    @Setter @Getter private String address = MachineUtils.IP_ADDRESS;
    @Setter @Getter private String hostName = MachineUtils.HOST_NAME;
    @Setter @Getter private String processId = MachineUtils.CURRENT_JVM_PID + "";
    @Setter @Getter private String message;

    @Override
    public <T> void merge(T data) {
        throw new UnsupportedOperationException();
    }

    public DTaskError(String taskId, String swimlaneId, String message) {
        this.taskId = taskId;
        this.swimlaneId = swimlaneId;
        this.message = message;
    }

}
