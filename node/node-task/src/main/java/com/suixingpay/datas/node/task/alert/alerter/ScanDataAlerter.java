package com.suixingpay.datas.node.task.alert.alerter;

import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月01日 21:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

public class ScanDataAlerter implements Alerter{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDataAlerter.class);
    @Override
    public void check(DataSourceWrapper source, DataSourceWrapper target) {
        LOGGER.info("check run");
        DbDialect sourceDialect = DbDialectFactory.INSTANCE.getDbDialect(source.getUniqueId());
        DbDialect targetDialect = DbDialectFactory.INSTANCE.getDbDialect(target.getUniqueId());
        //开始时间计算
        String startTime = "";
        //结束事件计算
        String endTime = "";
        //源端目标端数据库查询
        //更新对比时间
        //数据不一致时发送告警
        int countSource = 0;
        int countTarget = 1;
        if (countTarget != countSource) {
            String notice = new StringBuffer().append(startTime).append("至").append(endTime).append("\n\r")
                    .append("源端数据变更").append(countSource).append("条,").append("\n\r")
                    .append("目标端数据变更").append(countTarget).append("条。").append("\n\r")
                    .append("数据变更条目不一致，请尽快修正").toString();
            AlertProviderFactory.INSTANCE.notice(notice);
        }
    }
}
