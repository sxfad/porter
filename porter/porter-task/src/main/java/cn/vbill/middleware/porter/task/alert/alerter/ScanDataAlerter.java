/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package cn.vbill.middleware.porter.task.alert.alerter;

import cn.vbill.middleware.porter.common.node.statistics.NodeLog;
import cn.vbill.middleware.porter.common.task.statistics.DTaskStat;
import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.task.TaskContext;
import cn.vbill.middleware.porter.core.task.consumer.DataConsumer;
import cn.vbill.middleware.porter.core.task.loader.DataLoader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月24日 11:15
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月24日 11:15
 */
public class ScanDataAlerter implements Alerter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScanDataAlerter.class);
    private static final long TIME_SPAN_OF_MINUTES = 30;
    private FastDateFormat noticeDateFormat = FastDateFormat.getInstance("yyyyMMdd HH:mm:ss");

    /**
     * check
     *
     * @date 2018/8/9 下午2:03
     * @param: [consumer, loader, stat, checkMeta, receivers]
     * @return: void
     */
    public void check(DataConsumer consumer, DataLoader loader, DTaskStat stat, Triple<String[], String[],
            String[]> checkMeta) throws InterruptedException {
        LOGGER.debug("trying scan data");
        if (null == stat || !stat.getUpdateStat().get() || null == stat.getLastLoadedDataTime() || null == checkMeta.getRight()) {
            LOGGER.debug("null == stat ||  !stat.getUpdateStat().get() || null == stat.getLastLoadedTime() || null == checkMeta.getRight()");
            return;
        }

        //获得同步检查时间点并精确到分钟
        Date lastCheckedTime = stat.getLastCheckedTime();
        lastCheckedTime = null == lastCheckedTime ? stat.getRegisteredTime() : lastCheckedTime;
        lastCheckedTime = DateUtils.setSeconds(lastCheckedTime, 0);

        LOGGER.debug("获得同步检查时间点:{}", noticeDateFormat.format(lastCheckedTime));

        //计算与最同步时间间隔并推前5分钟,单位分钟
        long timeDiff = (DateUtils.addMinutes(stat.getLastLoadedDataTime(), 5).getTime()
                - lastCheckedTime.getTime()) / 1000 / 60;

        LOGGER.debug("计算与最新表记录操作时间间隔并推前5分钟:{},时间差:{}分钟", noticeDateFormat.format(stat.getLastLoadedDataTime()), timeDiff);

        //分割时间段
        int splitTimes = timeDiff > 0 ? (int) (timeDiff / TIME_SPAN_OF_MINUTES) : 0;

        LOGGER.debug("分割时间段:{}", splitTimes);

        if (splitTimes > 0) {
            for (int i = 0; i < splitTimes; i++) {
                //计算分钟差
                int startMinute = (int) (i * TIME_SPAN_OF_MINUTES + 1);
                int endMinute = (int) ((i + 1) * TIME_SPAN_OF_MINUTES);
                //计算开始时间，时间单位为yyyyMMdd HHmm:00
                Date startDate = DateUtils.addMinutes(lastCheckedTime, startMinute);
                startDate = DateUtils.setSeconds(startDate, 0);
                //计算结束时间，时间单位为yyyyMMdd HHmm:59
                Date endDate = DateUtils.addMinutes(lastCheckedTime, endMinute);
                endDate = DateUtils.setSeconds(endDate, 59);

                LOGGER.debug("执行同步检查逻辑,执行时间段:{}-{}", noticeDateFormat.format(startDate), noticeDateFormat.format(endDate));
                //执行同步检查逻辑，暂时为单线程模式执行
                checkLogic(stat, consumer, loader, startDate, endDate, checkMeta);
            }
            //更新同步时间点
            stat.setLastCheckedTime(DateUtils.addMinutes(lastCheckedTime, (int) (splitTimes * TIME_SPAN_OF_MINUTES) + 1));
        }
        LOGGER.debug("stat after scan data:{}", JSON.toJSONString(stat));
    }

    /**
     * checkLogic
     *
     * @date 2018/8/9 下午2:03
     * @param: [stat, consumer, loader, startDate, endDate, checkMeta, receivers]
     * @return: void
     */
    private void checkLogic(DTaskStat stat, DataConsumer consumer, DataLoader loader, Date startDate, Date endDate,
                            Triple<String[], String[], String[]> checkMeta) throws InterruptedException {
        //更新对比时间
        int countSource = consumer.getDataCount(checkMeta.getLeft()[0], checkMeta.getMiddle()[0], checkMeta.getRight()[0], startDate, endDate);
        int countTarget = loader.getDataCount(checkMeta.getLeft()[1], checkMeta.getMiddle()[1], checkMeta.getRight()[1], startDate, endDate);


        LOGGER.debug("执行同步检查逻辑,执行时间段:{}-{}, 结果:{}-{}", noticeDateFormat.format(startDate), noticeDateFormat.format(endDate),
                countSource, countTarget);

        //数据不一致时发送告警
        if (countTarget != countSource) {
            String notice = new StringBuffer().append(noticeDateFormat.format(startDate)).append("至").append(noticeDateFormat.format(endDate))
                    .append("\n\r")
                    .append("源端数据变更").append(countSource).append("条,").append("\n\r")
                    .append("目标端数据变更").append(countTarget).append("条。").append("\n\r")
                    .append("数据变更条目不一致，请尽快修正").toString();

            LOGGER.debug(notice);
            //更新同步检查次数
            stat.incrementAlertedTimes();
            String title = "[" + noticeDateFormat.format(new Date()) + "][" + MachineUtils.localhost() + ":" + MachineUtils.getPID() + "]数据同步告警";
            TaskContext.warning(new NodeLog(NodeLog.LogType.WARNING, notice).bindTaskId(TaskContext.trace().getTaskId())
                    .bindSwimlaneId(TaskContext.trace().getSwimlaneId()).upload(), title);
        }
    }

}
