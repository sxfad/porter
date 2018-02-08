/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.suixingpay.datas.common.config.Config;
import com.suixingpay.datas.common.config.ConfigType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class ZookeeperConfig   extends Config {
    @Setter @Getter private String url;
    @Setter @Getter private int sessionTimeout = 1000 * 60 * 10;

    public  ZookeeperConfig() {
        configType = ConfigType.ZOOKEEPER;
    }

    @Override
    protected void childStuff() {

    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }
}
