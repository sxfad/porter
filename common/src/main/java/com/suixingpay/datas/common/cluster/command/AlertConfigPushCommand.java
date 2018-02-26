/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.cluster.command;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.config.LogConfig;
import com.suixingpay.datas.common.config.TaskConfig;
import lombok.Getter;
import lombok.Setter;

/**
 * 管理后台配置推送
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 16:42
 */
public class AlertConfigPushCommand extends ConfigPushCommand  {
    @Getter @Setter private TaskConfig config;

    public AlertConfigPushCommand() {
        setType(ConfigPushType.ALERT);
    }

    @Override
    public String render() {
        return JSONObject.toJSONString(config);
    }
}
