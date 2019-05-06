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

package cn.vbill.middleware.porter.manager.config;

import cn.vbill.middleware.porter.common.cluster.config.ClusterConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月28日 10:17
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月28日 10:17
 */
@ConfigurationProperties(prefix = "manager")
@Component
public class ManagerConfig {
    private ClusterConfig cluster;

    /***允许默认定时GC***/
    private boolean gc = false;
    private Integer gcDelayOfMinutes = 30;
    /***允许默认定时GC***/


    /***允许任务出错重试***/
    private boolean taskRestart = true;
    /***允许任务出错重试***/


    public ClusterConfig getCluster() {
        return cluster;
    }

    public void setCluster(ClusterConfig cluster) {
        this.cluster = cluster;
    }

    public boolean isGc() {
        return gc;
    }

    public void setGc(boolean gc) {
        this.gc = gc;
    }

    public Integer getGcDelayOfMinutes() {
        return gcDelayOfMinutes;
    }

    public void setGcDelayOfMinutes(Integer gcDelayOfMinutes) {
        this.gcDelayOfMinutes = gcDelayOfMinutes;
    }

    public boolean isTaskRestart() {
        return taskRestart;
    }

    public void setTaskRestart(boolean taskRestart) {
        this.taskRestart = taskRestart;
    }
}
