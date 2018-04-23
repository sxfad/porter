/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Kudu配置
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class KuduConfig extends SourceConfig {
    private static final String SERVER_SPLIT_CHARACTER = ",";
    //ip:port
    @Setter @Getter private List<String> servers = new ArrayList<>();
    @Setter @Getter private int workerCount  = 10;

    public KuduConfig() {
        sourceType = SourceType.KUDU;
    }

    @Override
    protected void childStuff() {
        String serversStr = getProperties().getOrDefault("servers", "");
        if (!StringUtils.isBlank(serversStr)) {
            servers.addAll(Arrays.stream(serversStr.split(SERVER_SPLIT_CHARACTER)).collect(Collectors.toList()));
        }
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"servers"};
    }

    @Override
    protected boolean doCheck() {
        return true;
    }
}
