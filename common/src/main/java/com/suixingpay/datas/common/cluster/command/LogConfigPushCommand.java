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
import lombok.Getter;
import lombok.Setter;

/**
 * 推送日志配置 （日志等级推送）
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 16:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 16:42
 */
public class LogConfigPushCommand extends ConfigPushCommand {

    public LogConfigPushCommand() {
        setType(ConfigPushType.LOG);
    }

    public LogConfigPushCommand(LogConfig config) {
        this.config = config;
        setType(ConfigPushType.LOG);
    }

    @Getter
    @Setter
    private LogConfig config;

    @Override
    public String render() {
        return JSONObject.toJSONString(config);
    }
}
