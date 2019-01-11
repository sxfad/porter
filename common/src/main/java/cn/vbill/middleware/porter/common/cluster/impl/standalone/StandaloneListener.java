/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:21
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.impl.standalone;

import cn.vbill.middleware.porter.common.client.Client;
import cn.vbill.middleware.porter.common.client.impl.FileClient;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年10月19日 14:21
 * @version: V1.0
 * @review: zkevin/2018年10月19日 14:21
 */
public abstract class StandaloneListener extends AbstractClusterListener {
    protected FileClient client;
    @Override
    public void setClient(Client client) {
        this.client = (FileClient) client;
    }
}
