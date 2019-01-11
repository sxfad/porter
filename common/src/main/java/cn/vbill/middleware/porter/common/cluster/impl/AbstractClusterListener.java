/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年01月11日 14:51
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.impl;

import cn.vbill.middleware.porter.common.cluster.ClusterListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年01月11日 14:51
 * @version: V1.0
 * @review: zkevin/2019年01月11日 14:51
 */
public abstract class AbstractClusterListener implements ClusterListener {
    protected final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String PREFIX_ATALOG = "/suixingpay";
    public static final String BASE_CATALOG = PREFIX_ATALOG + "/datas";

    public abstract String listenPath();
    @Override
    public String getName() {
        return listenPath();
    }
}
