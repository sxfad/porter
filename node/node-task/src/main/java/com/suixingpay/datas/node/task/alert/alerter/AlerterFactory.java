/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:41
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.task.alert.alerter;

import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:41
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月01日 21:41
 */
@Component
@Scope("singleton")
public class AlerterFactory {
    private Alerter alerter;

    public AlerterFactory() {
        List<Alerter> alerters = SpringFactoriesLoader.loadFactories(Alerter.class, null);
        if (null != alerters && alerters.size() == 1) {
            alerter = alerters.get(0);
        } else {
            throw new RuntimeException("AlerterFactory仅允许配置单个Alerter实现");
        }
    }

    public void check(DataSourceWrapper source, DataSourceWrapper target) {
        alerter.check(source, target);
    }
}
