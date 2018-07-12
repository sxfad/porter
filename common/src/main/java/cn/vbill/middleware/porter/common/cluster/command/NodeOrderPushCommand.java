/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.command;

import com.alibaba.fastjson.JSONObject;
import cn.vbill.middleware.porter.common.config.NodeCommandConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 后台推送节点命令 （节点任务状态推送（运行中 or 暂停 ）|节点停止任务推送）
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 16:42
 */
public class NodeOrderPushCommand implements ClusterCommand {

    public NodeOrderPushCommand() {

    }

    public NodeOrderPushCommand(NodeCommandConfig config) {
        this.config = config;
    }

    @Getter
    @Setter
    private NodeCommandConfig config;

    public String render() {
        return JSONObject.toJSONString(config);
    }
}
