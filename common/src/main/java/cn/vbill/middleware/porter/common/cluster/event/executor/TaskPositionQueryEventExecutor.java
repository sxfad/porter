/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.event.executor;


import cn.vbill.middleware.porter.common.cluster.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventExecutor;
import cn.vbill.middleware.porter.common.cluster.event.ClusterListenerEventType;
import cn.vbill.middleware.porter.common.cluster.event.command.ClusterCommand;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskPositionQueryCommand;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiConsumer;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 15:47
 * @version: V1.0
 * @review: zkevin/2019年03月14日 15:47
 */
public class TaskPositionQueryEventExecutor extends ClusterListenerEventExecutor {

    public TaskPositionQueryEventExecutor(Class bindClass, String treeNodePath) {
        super(bindClass, ClusterListenerEventType.TaskPositionQuery);
        bind(getBlock(treeNodePath));
    }

    @SneakyThrows
    private BiConsumer<ClusterCommand, ClusterClient> getBlock(String treeNodePath) {
        return (clusterCommand, client) -> {
            TaskPositionQueryCommand command = (TaskPositionQueryCommand) clusterCommand;

            String positionPath = treeNodePath + "/" + command.getTaskId() + "/position/" + command.getSwimlaneId();
            ClusterClient.TreeNode positionPair = client.isExists(positionPath, false) ? client.getData(positionPath) : null;
            String position = null != positionPair && !StringUtils.isBlank(positionPair.getData())
                    ? positionPair.getData() : StringUtils.EMPTY;
            if (null != command.getCallback()) {
                command.getCallback().callback(position);
            }
        };
    }
}
