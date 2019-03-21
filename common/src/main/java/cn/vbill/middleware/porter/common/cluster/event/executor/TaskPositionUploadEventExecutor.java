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
import cn.vbill.middleware.porter.common.cluster.event.command.TaskPositionUploadCommand;
import cn.vbill.middleware.porter.common.exception.TaskStopTriggerException;
import lombok.SneakyThrows;

import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskPositionUploadEventExecutor extends ClusterListenerEventExecutor {

    public TaskPositionUploadEventExecutor(Class bindClass, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskPositionUpload);
        bind(getBlock(treeNodePath));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath) {
        return new BiConsumer<ClusterCommand, ClusterClient>() {
            @SneakyThrows
            public void accept(ClusterCommand clusterCommand, ClusterClient client) {
                TaskPositionUploadCommand command = (TaskPositionUploadCommand) clusterCommand;
                //自旋获得ZK链接
                client.clientSpinning();
                //如果仍不能获得数据库连接
                if (!client.alive()) {
                    throw new TaskStopTriggerException("节点集群客户端链接失效");
                }
                String position = treeNodePath + "/" + command.getTaskId() + "/position/" + command.getSwimlaneId();
                client.changeData(position, false, false, command.getPosition());
            }
        };
    }
}
