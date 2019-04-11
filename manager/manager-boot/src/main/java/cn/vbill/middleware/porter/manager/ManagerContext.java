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

import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
@SuppressWarnings("unchecked")
public enum ManagerContext {

    /**
     * INSTANCE
     */
    INSTANCE();

    private ApplicationContext context;
    private final Map<List<String>, String> taskErrorMarked = new ConcurrentHashMap<>();
    private volatile WarningReceiver[] receivers = new WarningReceiver[0];

    /**
     * 获取Bean
     *
     * @date 2018/8/9 下午4:14
     * @param: [clazz]
     * @return: T
     */
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    /**
     * newStoppedTask
     *
     * @date 2018/8/9 下午4:15
     * @param: [taskId, swimlaneId]
     * @return: void
     */
    public void newStoppedTask(List<String> key, String message) {
        taskErrorMarked.put(key, message);
    }

    /**
     * removeStoppedTask
     *
     * @date 2018/8/9 下午4:15
     * @param: [taskId, swimlaneId]
     * @return: void
     */
    public void removeStoppedTask(List<String> key) {
        taskErrorMarked.remove(key);
    }

    public List<String> getStoppedTasks() {
        return Arrays.asList(taskErrorMarked.values().toArray(new String[0]));
    }



    public synchronized void addWarningReceivers(WarningReceiver[] newReceivers) {
        List<WarningReceiver> tmp = new ArrayList<>(Arrays.asList(receivers));
        tmp.addAll(Arrays.asList(newReceivers));
        receivers = tmp.toArray(new WarningReceiver[0]);
    }

    public WarningReceiver[] getReceivers() {
        return receivers;
    }
}
