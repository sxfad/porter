/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:14
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.node.boot.config;

import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.cluster.command.TaskRegisterCommand;
import com.suixingpay.datas.common.task.Task;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:14
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 10:14
 */
@ConfigurationProperties(prefix = "task")
@Component
public class TaskConfig {
    private List<Task> items;
    public TaskConfig() {
        items = new ArrayList<>();
    }

    public List<Task> getItems() {
        return items;
    }

    public void setItems(List<Task> items) {
        this.items = items;
    }

    public List<ClusterCommand> convert2RegisterCmd() {
        List<ClusterCommand> cmdList = new ArrayList<>();
        if (null != items && ! items.isEmpty()) {
            for (Task t : items) {
                String[] topics = t.listTopic();
                if (null != topics && topics.length > 0) {
                    for (String topic : topics) {
                        cmdList.add(new TaskRegisterCommand(t.getTaskId(), topic));
                    }
                }
            }
        }
        return cmdList;
    }
}