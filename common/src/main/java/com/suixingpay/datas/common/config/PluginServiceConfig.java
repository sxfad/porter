/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月25日 10:28
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import org.apache.commons.lang3.StringUtils;

/**
 * 插件服务配置文件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年04月25日 10:28
 * @version: V1.0
 * @review: zkevin/2018年04月25日 10:28
 */
public interface PluginServiceConfig {
    /**
     * 用于插件服务客户端加载匹配
     * @param configName
     * @return
     */
    default boolean isMatch(String configName) {
        return StringUtils.trimToEmpty(getConfigName()).equals(StringUtils.trimToEmpty(configName));
    }

    /**
     * 获取客户端名称
     * @return
     */
    String getConfigName();

    /**
     * 获取配置文件目标用户名称
     * @return
     */
    String getTargetName();
}
