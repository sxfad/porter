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

package cn.vbill.middleware.porter.manager.core.icon;

import java.util.Date;
import java.util.List;

import cn.vbill.middleware.porter.manager.core.entity.MrJobTasksMonitor;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class MrJobMonitor {

    public MrJobMonitor(List<MrJobTasksMonitor> list) {

        int number = list.size();
        /**
         * 时间字符串
         */
        this.xAxisData = new Date[number];
        /**
         * 插入成功.
         */
        this.insertSucces = new Long[number];
        /**
         * 插入失败.
         */
        this.insertFailure = new Long[number];

        /**
         * 更新成功.
         */
        this.updateSucces = new Long[number];

        /**
         * 更新失败.
         */
        this.updateFailure = new Long[number];

        /**
         * 删除成功.
         */
        this.deleteSucces = new Long[number];

        /**
         * 删除失败.
         */
        this.deleteFailure = new Long[number];

        /**
         * 告警次数
         */
        this.alarmNumber = new Long[number];

        for (int i = 0; i < list.size(); i++) {
            /**
             * 时间字符串
             */
            this.xAxisData[i] = list.get(i).getMonitorDate();
            /**
             * 插入成功.
             */
            this.insertSucces[i] = list.get(i).getInsertSucces();
            /**
             * 插入失败.
             */
            this.insertFailure[i] = list.get(i).getInsertFailure();

            /**
             * 更新成功.
             */
            this.updateSucces[i] = list.get(i).getUpdateSucces();

            /**
             * 更新失败.
             */
            this.updateFailure[i] = list.get(i).getUpdateFailure();

            /**
             * 删除成功.
             */
            this.deleteSucces[i] = list.get(i).getDeleteSucces();

            /**
             * 删除失败.
             */
            this.deleteFailure[i] = list.get(i).getDeleteFailure();

            /**
             * 告警次数
             */
            this.alarmNumber[i] = list.get(i).getAlarmNumber();
        }
    }

    /**
     * 时间字符串
     */
    private Date[] xAxisData;

    /**
     * 插入成功.
     */
    private Long[] insertSucces;

    /**
     * 插入失败.
     */
    private Long[] insertFailure;

    /**
     * 更新成功.
     */
    private Long[] updateSucces;

    /**
     * 更新失败.
     */
    private Long[] updateFailure;

    /**
     * 删除成功.
     */
    private Long[] deleteSucces;

    /**
     * 删除失败.
     */
    private Long[] deleteFailure;

    /**
     * 告警次数
     */
    private Long[] alarmNumber;

    /**
     * AxisData get方法
     * @return
     */
    public Date[] getxAxisData() {
        return xAxisData;
    }

    /**
     * AxisData set方法
     * @param xAxisData
     */
    public void setxAxisData(Date[] xAxisData) {
        this.xAxisData = xAxisData;
    }

    public Long[] getInsertSucces() {
        return insertSucces;
    }

    public void setInsertSucces(Long[] insertSucces) {
        this.insertSucces = insertSucces;
    }

    public Long[] getInsertFailure() {
        return insertFailure;
    }

    public void setInsertFailure(Long[] insertFailure) {
        this.insertFailure = insertFailure;
    }

    public Long[] getUpdateSucces() {
        return updateSucces;
    }

    public void setUpdateSucces(Long[] updateSucces) {
        this.updateSucces = updateSucces;
    }

    public Long[] getUpdateFailure() {
        return updateFailure;
    }

    public void setUpdateFailure(Long[] updateFailure) {
        this.updateFailure = updateFailure;
    }

    public Long[] getDeleteSucces() {
        return deleteSucces;
    }

    public void setDeleteSucces(Long[] deleteSucces) {
        this.deleteSucces = deleteSucces;
    }

    public Long[] getDeleteFailure() {
        return deleteFailure;
    }

    public void setDeleteFailure(Long[] deleteFailure) {
        this.deleteFailure = deleteFailure;
    }

    public Long[] getAlarmNumber() {
        return alarmNumber;
    }

    public void setAlarmNumber(Long[] alarmNumber) {
        this.alarmNumber = alarmNumber;
    }

}
