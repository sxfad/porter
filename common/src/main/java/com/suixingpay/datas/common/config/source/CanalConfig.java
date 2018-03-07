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
import org.apache.commons.lang3.math.NumberUtils;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class CanalConfig extends SourceConfig {
    private static final String ADDRESS_SPLIT_CHARACTER = ",";
    private static final String ADDRESS_ITEM_SPLIT_CHARACTER = ":";
    //优先zookeeper
    @Setter @Getter private String zkServers;
    @Setter @Getter private List<InetSocketAddress> addresses = new ArrayList<>();
    //实例描述
    @Setter @Getter private String destination;
    @Setter @Getter private String username;
    @Setter @Getter private String password;

    @Setter @Getter private String filter;
    @Setter @Getter private int oncePollSize = 1000;
    @Setter @Getter private boolean autoCommit = Boolean.FALSE;

    public CanalConfig() {
        sourceType = SourceType.CANAL;
    }

    @Override
    protected void childStuff() {
        String addressesStr = getProperties().getOrDefault("addresses", "");
        if (!StringUtils.isBlank(addressesStr)) {
            String[] addressArray = addressesStr.split(ADDRESS_SPLIT_CHARACTER);
            if (null != addressArray && addressArray.length > 0) {
                Arrays.stream(addressArray).filter(i -> !StringUtils.isBlank(i)).forEach(i -> {
                    String[] item = i.split(ADDRESS_ITEM_SPLIT_CHARACTER);
                    if (null != item && item.length == 2 && NumberUtils.isCreatable(item[1])) {
                        addresses.add(new InetSocketAddress(item[0], Integer.parseInt(item[1])));
                    }
                });
            }
        }
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"addresses"};
    }
}
