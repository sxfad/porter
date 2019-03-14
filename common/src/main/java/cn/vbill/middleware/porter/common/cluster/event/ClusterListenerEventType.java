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
package cn.vbill.middleware.porter.common.cluster.event;

/**
 * 集群监听事件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月13日 16:16
 * @version: V1.0
 * @review: zkevin/2019年03月13日 16:16
 */
public enum ClusterListenerEventType {
    /**配置文件提交**/
    ConfigPush,
    /**后台推送节点命令 （节点任务状态推送（运行中 or 暂停 ）|节点停止任务推送**/
    NodeOrderPush,
    /**节点注册（服务器上报zk）**/
    NodeRegister,
    /**node节点shutdown**/
    Shutdown,
    /**统计信息上传**/
    StatisticUpload,
    /**任务已被分配**/
    TaskAssigned,
    /**任务点位上传**/
    TaskPosition,
    /**任务提交**/
    TaskPush,
    /**任务注册**/
    TaskRegister,
    /**任务状态查询**/
    TaskStatQuery,
    /**任务状态上传**/
    TaskStatUpload,
    /**任务停止**/
    TaskStop,
    /**任务异常停止**/
    TaskStoppedByError,
    TaskPositionQuery,
    TaskPositionUpload;
}
