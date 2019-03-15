/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 17:42
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.cluster;

import cn.vbill.middleware.porter.common.client.ClusterClient;
import cn.vbill.middleware.porter.common.cluster.data.DNode;
import cn.vbill.middleware.porter.common.cluster.data.DTaskLock;
import cn.vbill.middleware.porter.core.NodeContext;
import com.alibaba.fastjson.JSONObject;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月14日 17:42
 * @version: V1.0
 * @review: zkevin/2019年03月14日 17:42
 */
@AllArgsConstructor
public class CommonCodeBlock {

    private ClusterClient client;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean nodeAssignCheck(String path) {
        try {
            if (!NodeContext.INSTANCE.forceAssign()) return false;
            ClusterClient.TreeNode treeNode = client.getData(path);
            if (null != treeNode && StringUtils.isNotBlank(treeNode.getData())) {
                DNode nodeInfo = JSONObject.parseObject(treeNode.getData(), DNode.class);
                if (nodeInfo.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) //节点Id相符
                        && nodeInfo.getAddress().equals(NodeContext.INSTANCE.getAddress())) { //IP地址相符
                    client.delete(path);
                    return true;
                }
            }
        } catch (Exception e) {
            logger.warn("尝试删除节点占用");
        }
        return false;
    }

    public boolean taskAssignCheck(String path) {
        try {
            if (!NodeContext.INSTANCE.forceAssign()) return false;
            ClusterClient.TreeNode treeNode = client.getData(path);
            if (null != treeNode && StringUtils.isNotBlank(treeNode.getData())) {
                DTaskLock lockInfo = JSONObject.parseObject(treeNode.getData(), DTaskLock.class);
                if (lockInfo.getNodeId().equals(NodeContext.INSTANCE.getNodeId()) //节点Id相符
                        && lockInfo.getAddress().equals(NodeContext.INSTANCE.getAddress())) { //IP地址相符
                    client.delete(path);
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }
}
