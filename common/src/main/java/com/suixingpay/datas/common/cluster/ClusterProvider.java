/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:10
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster;

import com.suixingpay.datas.common.cluster.command.ClusterCommand;
import com.suixingpay.datas.common.config.ClusterConfig;
import com.suixingpay.datas.common.dic.ClusterPlugin;
import com.suixingpay.datas.common.task.TaskEventListener;

import java.io.IOException;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月07日 11:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月07日 11:10
 */
public interface ClusterProvider {
    /**
     * 用于集群监听到任务事件的分发
     * @param listener
     */
    void addTaskEventListener(TaskEventListener listener);

    /**
     * 用于集群监听到任务事件的分发
     * @param listener
     */
    void removeTaskEventListener(TaskEventListener listener);

    /**
     * 启动集群模块
     * @throws IOException
     */
    void start(ClusterConfig config) throws Exception;

    /**
     * 匹配配置文件指定的集群实现
     * @param type
     * @return
     */
    boolean matches(ClusterPlugin type);

    /**
     * 退出集群
     * 需在业务代码执行之后才能执行,进程退出Hook
     */
    void stop();

    /**
     * 命令广播，用于在别的模块通知集群的相关模块
     * @param command
     * @throws Exception
     */
    void broadcastCommand(ClusterCommand command) throws Exception;

    /**
     * 集群插件是否有效
     */
    boolean available();
}
