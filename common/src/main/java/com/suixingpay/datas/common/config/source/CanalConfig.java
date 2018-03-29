/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config.source;

import com.alibaba.otter.canal.instance.manager.model.CanalParameter;
import com.suixingpay.datas.common.config.SourceConfig;
import com.suixingpay.datas.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月02日 14:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月02日 14:24
 */
public class CanalConfig extends SourceConfig {
    //实例描述
    @Setter @Getter private Long canalId = System.nanoTime();
    @Setter @Getter private String canalName = UUID.randomUUID().toString();
    @Setter @Getter private CanalParameter.SourcingType sourcingType = CanalParameter.SourcingType.MYSQL;
    @Setter @Getter private Long slaveId;

    @Setter @Getter private String address;
    @Setter @Getter private String database;
    @Setter @Getter private String username;
    @Setter @Getter private String password;
    @Setter @Getter private String filter = "";
    @Setter @Getter private int oncePollSize = 1000;

    public CanalConfig() {
        sourceType = SourceType.CANAL;
    }

    @Override
    protected void childStuff() {
    }

    public InetSocketAddress getSocketAddress() {
        String[] addressArray = address.split(":");
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
    public String getSwimlaneId() {
        return database;
    }
}
