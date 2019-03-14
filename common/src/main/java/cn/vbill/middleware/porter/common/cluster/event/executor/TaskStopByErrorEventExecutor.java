/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.event.executor;


import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskStoppedByErrorCommand;
import lombok.SneakyThrows;

import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskStopByErrorEventExecutor extends ClusterListenerEventExecutor {

    public TaskStopByErrorEventExecutor(Class bindClass, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskPush);
        bind(getBlock(treeNodePath));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath) {
        return (clusterCommand, client) -> {
            TaskStoppedByErrorCommand command = (TaskStoppedByErrorCommand) clusterCommand;
            String errorPath = treeNodePath + "/" + command.getTaskId() + "/error/" + command.getSwimlaneId();
            client.create(errorPath, command.getMsg(), false, false);
        };
    }
}
