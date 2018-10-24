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

/**
 *
 * @author: 付紫钲
 * @date: 2018/4/26
 */
public class HomeBlockResult {

    /**
     * 10分钟内计数
     */
    private Integer tenMinutesCount;

    /**
     * 1小时内计数
     */
    private Integer oneHourCount;

    /**
     * 24小时内计数
     */
    private Integer twentyFourHourCount;

    /**
     * 工作中任务计数
     */
    private Integer tasksWorkingCount;

    /**在线节点.*/
    private Integer nodeNum1 = 0;

    /**在线节点并且运行中.*/
    private Integer nodeNum2 = 0;

    /**节点健康状况-正常.*/
    private Integer mrNodeNum1 = 0;

    /**节点健康状况-需要关注.*/
    private Integer mrNodeNum2 = 0;

    /**节点健康状况-异常.*/
    private Integer mrNodeNum3 = 0;

    /**
     * 10分钟内计数 get方法
     */
    public Integer getTenMinutesCount() {
        return tenMinutesCount;
    }

    /**
     * 10分钟内计数 set方法
     */
    public void setTenMinutesCount(Integer tenMinutesCount) {
        this.tenMinutesCount = tenMinutesCount;
    }

    /**
     * 1小时内计数 get方法
     */
    public Integer getOneHourCount() {
        return oneHourCount;
    }

    /**
     * 1小时内计数 set方法
     */
    public void setOneHourCount(Integer oneHourCount) {
        this.oneHourCount = oneHourCount;
    }

    /**
     * 24小时内计数 get方法
     */
    public Integer getTwentyFourHourCount() {
        return twentyFourHourCount;
    }

    /**
     * 24小时内计数 set方法
     */
    public void setTwentyFourHourCount(Integer twentyFourHourCount) {
        this.twentyFourHourCount = twentyFourHourCount;
    }

    /**
     * 工作中任务计数 get方法
     */
    public Integer getTasksWorkingCount() {
        return tasksWorkingCount;
    }

    /**
     * 工作中任务计数 set方法
     */
    public void setTasksWorkingCount(Integer tasksWorkingCount) {
        this.tasksWorkingCount = tasksWorkingCount;
    }

    public Integer getNodeNum1() {
        return nodeNum1;
    }

    public void setNodeNum1(Integer nodeNum1) {
        this.nodeNum1 = nodeNum1;
    }

    public Integer getNodeNum2() {
        return nodeNum2;
    }

    public void setNodeNum2(Integer nodeNum2) {
        this.nodeNum2 = nodeNum2;
    }

    public Integer getMrNodeNum1() {
        return mrNodeNum1;
    }

    public void setMrNodeNum1(Integer mrNodeNum1) {
        this.mrNodeNum1 = mrNodeNum1;
    }

    public Integer getMrNodeNum2() {
        return mrNodeNum2;
    }

    public void setMrNodeNum2(Integer mrNodeNum2) {
        this.mrNodeNum2 = mrNodeNum2;
    }

    public Integer getMrNodeNum3() {
        return mrNodeNum3;
    }

    public void setMrNodeNum3(Integer mrNodeNum3) {
        this.mrNodeNum3 = mrNodeNum3;
    }

    
}
