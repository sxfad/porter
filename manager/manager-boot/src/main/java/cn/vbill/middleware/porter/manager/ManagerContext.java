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

package cn.vbill.middleware.porter.manager;

import org.apache.commons.collections.list.TreeList;
import org.springframework.context.ApplicationContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
public enum ManagerContext {
    INSTANCE();
    private ApplicationContext context;
    private final Map<String, List<String>> stoppedTask = new ConcurrentHashMap<>();

    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }


    public void newStoppedTask(String taskId, String swimlaneId) {
        stoppedTask.computeIfAbsent(taskId, key -> new TreeList()).add(swimlaneId);
    }

    public void removeStoppedTask(String taskId, String swimlaneId) {
        List<String> swimlanes = stoppedTask.computeIfAbsent(taskId, key -> new TreeList());
        swimlanes.remove(swimlaneId);
        if (swimlanes.isEmpty()) {
            stoppedTask.remove(taskId);
        }
    }

    public Map<String, List<String>> getStoppedTasks() {
        return Collections.unmodifiableMap(stoppedTask);
    }
}
