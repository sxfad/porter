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

package cn.vbill.middleware.porter.manager.service;

/**
 * @author: 付紫钲
 * @date: 2018/4/25
 */
public interface MonitorScheduledService {

    /**
     * 备份源表数据
     *
     * @date 2018/8/10 上午11:56
     * @param: []
     * @return: void
     */
    void transferDataTask();

    /**
     * 删除存在30天的表
     *
     * @date 2018/8/10 上午11:56
     * @param: []
     * @return: void
     */
    void dropTableTask();

    /**
     * 创建后天的表并且判断如果明天的表没创建则创建明天的表
     */
    void createTableTask();

    /**
     * 在项目初始化时 创建今天和明天的监控表
     */
    void initMonitorTable();
}
