/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月25日 10:28
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.client;


import org.apache.commons.lang3.StringUtils;

/**
 * 插件服务客户端接口
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月25日 10:28
 * @version: V1.0
 * @review: zkevin/2018年04月25日 10:28
 */
public interface PluginServiceClient {
    /**
     * 用于插件服务客户端加载匹配
     * @param clientName
     * @return
     */
    default boolean isMatch(String clientName) {
        return StringUtils.trimToEmpty(getClientName()).equals(StringUtils.trimToEmpty(clientName));
    }

    /**
     * 获取客户端名称
     * @return
     */
    String getClientName();
}
