package com.suixingpay.datas.node.task.alert.alerter;

import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.common.datasource.DataSourceWrapper;
import com.suixingpay.datas.node.core.db.dialect.DbDialect;
import com.suixingpay.datas.node.core.db.dialect.DbDialectFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private static final long TIME_SPAN_OF_MINUTES = 30 ;
    private static final DateFormat NOTICE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");


    @Override
    public void check(DataSourceWrapper source, DataSourceWrapper target, DTaskStat stat, Triple<String[], String[], String[]> checkMeta) {
        if (null == stat ||  ! stat.getUpdateStat().get() || null == stat.getLastLoadedTime()) return;
        //数据库连接
        DbDialect sourceDialect = DbDialectFactory.INSTANCE.getDbDialect(source.getUniqueId());
        DbDialect targetDialect = DbDialectFactory.INSTANCE.getDbDialect(target.getUniqueId());

        //获得同步检查时间点并精确到分钟
        Date lastCheckedTime = stat.getLastCheckedTime();
        lastCheckedTime = null == lastCheckedTime ? stat.getStatedTime() : lastCheckedTime;
        lastCheckedTime = DateUtils.setSeconds(lastCheckedTime, 0);

        //计算与最同步时间间隔并推前5分钟,单位分钟
        long timeDiff = (DateUtils.addMinutes(stat.getLastLoadedTime(),5).getTime() - lastCheckedTime.getTime())
                / 1000 / 60;
        //分割时间段
        int splitTimes = timeDiff > 0 ? (int) (timeDiff / TIME_SPAN_OF_MINUTES) : 0;
        if (splitTimes > 0) {
            for (int i = 0; i < splitTimes; i ++) {
                //计算分钟差
                int startMinute = (int) (i * TIME_SPAN_OF_MINUTES + 1);
                int endMinute = (int) ((i + 1) * TIME_SPAN_OF_MINUTES);
                //计算开始时间，时间单位为yyyyMMdd HHmm:00
                Date startDate = DateUtils.addMinutes(lastCheckedTime, startMinute);
                startDate = DateUtils.setSeconds(startDate, 0);
                //计算结束时间，时间单位为yyyyMMdd HHmm:59
                Date endDate = DateUtils.addMinutes(lastCheckedTime, endMinute);
                endDate = DateUtils.setSeconds(endDate, 59);
                //执行同步检查逻辑，暂时为单线程模式执行
                checkLogic(sourceDialect, targetDialect, startDate, endDate, checkMeta);
            }
        }
        //更新同步时间点
        stat.setLastCheckedTime(DateUtils.addMinutes(lastCheckedTime, (int) (timeDiff * TIME_SPAN_OF_MINUTES)));
        //更新同步检查册书
        stat.getAlertedTimes().incrementAndGet();
    }

    private void checkLogic(DbDialect source, DbDialect target, Date startDate, Date endDate, Triple<String[], String[], String[]> checkMeta) {
        //源端目标端数据库查询
        String sourceSql = source.getSqlTemplate().getDataChangedCountSql(checkMeta.getLeft()[0],checkMeta.getMiddle()[0], checkMeta.getRight()[0]);
        String targetSql = target.getSqlTemplate().getDataChangedCountSql(checkMeta.getLeft()[1],checkMeta.getMiddle()[1], checkMeta.getRight()[1]);
        //更新对比时间
        int countSource = countQuery(source.getJdbcTemplate(), sourceSql, startDate, endDate);

        int countTarget = countQuery(target.getJdbcTemplate(), targetSql, startDate, endDate);


        //数据不一致时发送告警
        if (countTarget != countSource) {
            String notice = new StringBuffer().append(NOTICE_DATE_FORMAT.format(startDate)).append("至").append(NOTICE_DATE_FORMAT.format(endDate)).append("\n\r")
                    .append("源端数据变更").append(countSource).append("条,").append("\n\r")
                    .append("目标端数据变更").append(countTarget).append("条。").append("\n\r")
                    .append("数据变更条目不一致，请尽快修正").toString();
            AlertProviderFactory.INSTANCE.notice(notice);
        }
    }

    private int countQuery(JdbcTemplate template, String sql, Date startDate, Date endDate) {
        //数组形式仅仅是为了处理回调代码块儿对final局部变量的要求
        int[] count = {0};
        template.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                if (null != rs && rs.first()) {
                    String value = rs.getString(0);
                    if (!StringUtils.isBlank(value) && StringUtils.isNumeric(value)) {
                        count[0] = Integer.valueOf(value).intValue();
                    }
                }
            }
        }, startDate, endDate);
        return  count[0];
    }
}
