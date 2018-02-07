package com.suixingpay.datas.node.task.alert.alerter;

import com.alibaba.fastjson.JSON;
import com.suixingpay.datas.common.alert.AlertProviderFactory;
import com.suixingpay.datas.common.cluster.data.DTaskStat;
import com.suixingpay.datas.node.core.consumer.DataConsumer;
import com.suixingpay.datas.node.core.loader.DataLoader;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public void check(DataConsumer consumer, DataLoader loader, DTaskStat stat, Triple<String[], String[], String[]> checkMeta) {
        LOGGER.debug("trying scan data");
        if (null == stat ||  ! stat.getUpdateStat().get() || null == stat.getLastLoadedTime() || null == checkMeta.getRight()) {
            LOGGER.debug("null == stat ||  ! stat.getUpdateStat().get() || null == stat.getLastLoadedTime() || null == checkMeta.getRight()");
            return;
        }

        //获得同步检查时间点并精确到分钟
        Date lastCheckedTime = stat.getLastCheckedTime();
        lastCheckedTime = null == lastCheckedTime ? stat.getStatedTime() : lastCheckedTime;
        lastCheckedTime = DateUtils.setSeconds(lastCheckedTime, 0);

        LOGGER.debug("获得同步检查时间点:{}", NOTICE_DATE_FORMAT.format(lastCheckedTime));

        //计算与最同步时间间隔并推前5分钟,单位分钟
        long timeDiff = (DateUtils.addMinutes(stat.getLastLoadedTime(),5).getTime() - lastCheckedTime.getTime())
                / 1000 / 60;

        LOGGER.debug("计算与最新表记录操作时间间隔并推前5分钟:{},时间差:{}分钟", NOTICE_DATE_FORMAT.format(stat.getLastLoadedTime()) , timeDiff);

        //分割时间段
        int splitTimes = timeDiff > 0 ? (int) (timeDiff / TIME_SPAN_OF_MINUTES) : 0;

        LOGGER.debug("分割时间段:{}", splitTimes);

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

                LOGGER.debug("执行同步检查逻辑,执行时间段:{}-{}", NOTICE_DATE_FORMAT.format(startDate), NOTICE_DATE_FORMAT.format(endDate));

                //执行同步检查逻辑，暂时为单线程模式执行
                checkLogic(stat,consumer, loader, startDate, endDate, checkMeta);
            }
            //更新同步时间点
            stat.setLastCheckedTime(DateUtils.addMinutes(lastCheckedTime, (int) (splitTimes * TIME_SPAN_OF_MINUTES) + 1));
        }
        LOGGER.debug("stat after scan data:{}", JSON.toJSONString(stat));
    }

    private void checkLogic(DTaskStat stat, DataConsumer consumer, DataLoader loader, Date startDate, Date endDate, Triple<String[], String[], String[]> checkMeta) {
        //更新对比时间
        int countSource = consumer.getDataCount(checkMeta.getLeft()[0],checkMeta.getMiddle()[0], checkMeta.getRight()[0], startDate, endDate);
        int countTarget = loader.getDataCount(checkMeta.getLeft()[1],checkMeta.getMiddle()[1], checkMeta.getRight()[1], startDate, endDate);


        LOGGER.debug("执行同步检查逻辑,执行时间段:{}-{}, 结果:{}-{}", NOTICE_DATE_FORMAT.format(startDate), NOTICE_DATE_FORMAT.format(endDate), countSource, countTarget);

        //数据不一致时发送告警
        if (countTarget != countSource) {
            String notice = new StringBuffer().append(NOTICE_DATE_FORMAT.format(startDate)).append("至").append(NOTICE_DATE_FORMAT.format(endDate)).append("\n\r")
                    .append("源端数据变更").append(countSource).append("条,").append("\n\r")
                    .append("目标端数据变更").append(countTarget).append("条。").append("\n\r")
                    .append("数据变更条目不一致，请尽快修正").toString();

            LOGGER.debug(notice.toString());
            AlertProviderFactory.INSTANCE.notice(notice);
            //更新同步检查次数
            stat.incrementAlertedTimes();
        }
    }

}
