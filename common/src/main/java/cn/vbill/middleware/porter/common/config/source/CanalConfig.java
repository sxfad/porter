/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config.source;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class CanalConfig extends SourceConfig {
    private static final String ADDRESS_SPLIT_CHARACTER = ":";

    //实例描述
    @Setter @Getter private Long canalId = System.nanoTime();
    @Setter @Getter private String canalName = UUID.randomUUID().toString();
    @Setter @Getter private CanalParameter.SourcingType sourcingType = CanalParameter.SourcingType.MYSQL;
    @Setter @Getter private Long slaveId = System.nanoTime();

    @Setter @Getter private String address;
    @Setter @Getter private String database;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private String filter = "";
    @Setter @Getter private int oncePollSize = 1000;
    @Setter @Getter private int pollTimeOut  = -1;

    public CanalConfig() {
        sourceType = SourceType.CANAL;
    }

    @Override
    protected void childStuff() {
    }

    public InetSocketAddress getSocketAddress() {
        String[] addressArray = address.split(ADDRESS_SPLIT_CHARACTER);
        if (null != addressArray && addressArray.length == 2) {
            return new InetSocketAddress(addressArray[0], Integer.parseInt(addressArray[1]));
        }
        return null;
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }

    @Override
    protected boolean doCheck() {
        return !StringUtils.isBlank(address) && address.split(ADDRESS_SPLIT_CHARACTER).length == 2;
    }


    @Override
    public String getSwimlaneId() {
        return database;
    }
}
