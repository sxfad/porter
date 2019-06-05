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

package cn.vbill.middleware.porter.common.task.event;

import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import lombok.Getter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月21日 17:19
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月21日 17:19
 */
public class TaskOwnerEvent extends TaskEvent {
    @Getter private final WarningOwner owner;
    @Getter private final String taskId;

    public TaskOwnerEvent(WarningOwner event, String taskId) {
        super(TaskEventType.TASK_OWNER);
        this.owner = event;
        this.taskId = taskId;

    }
}
