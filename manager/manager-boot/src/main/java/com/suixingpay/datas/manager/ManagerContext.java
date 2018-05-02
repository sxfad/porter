/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.manager;

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
    private final Map<String, List<String>> stoppedTask= new ConcurrentHashMap<>();

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
