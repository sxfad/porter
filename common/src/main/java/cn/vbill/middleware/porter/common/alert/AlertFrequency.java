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

package cn.vbill.middleware.porter.common.alert;

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

    /**
     * updateFrequency
     *
     * @date 2018/8/10 下午2:45
     * @param: [prevContent]
     * @return: void
     */
    public void updateFrequency(String prevContent) {
        try {
            nodeLock.writeLock().lock();
            stat.setPrevContent(prevContent);
            stat.setPrevDate(new Date());
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    /**
     * canSend
     *
     * @author FuZizheng
     * @date 2018/8/10 下午2:45
     * @param: [content]
     * @return: boolean
     */
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
