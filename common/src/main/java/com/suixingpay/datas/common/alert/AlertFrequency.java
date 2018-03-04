/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月02日 11:35
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月02日 11:35
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月02日 11:35
 */
public class AlertFrequency {
    private final ReadWriteLock nodeLock = new ReentrantReadWriteLock();
    private final FrequencyStat stat = new FrequencyStat();
    @Setter private volatile int frequencyOfSecond = 60;

    public void updateFrequency(String prevContent) {
        try {
            nodeLock.writeLock().lock();
            stat.setPrevContent(prevContent);
            stat.setPrevDate(new Date());
        } finally {
            nodeLock.writeLock().unlock();
        }
    }
    public boolean canSend(String content) {
        try {
            nodeLock.readLock().lock();
            return null == stat.prevDate || null == stat.prevContent || StringUtils.trimToEmpty(content).hashCode()
                    != StringUtils.trimToEmpty(stat.prevContent).hashCode() || DateUtils.addSeconds(stat.prevDate, frequencyOfSecond)
                    .before(new Date());
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    private class FrequencyStat {
        @Setter private Date prevDate;
        @Setter private String prevContent;
    }
}
