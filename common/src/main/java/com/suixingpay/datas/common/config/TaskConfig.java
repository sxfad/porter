/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 18:10
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 18:10
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 18:10
 */
public class TaskConfig extends Config {
    @Setter @Getter private String taskId;
    @Setter @Getter private DataConsumerConfig consumer;
    @Setter @Getter private DataLoaderConfig loader;
    @Setter @Getter private List<TableMapperConfig> mapper = new ArrayList<>();
    @Override
    protected void childStuff() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[] {"consumer", "loader"};
    }
}
