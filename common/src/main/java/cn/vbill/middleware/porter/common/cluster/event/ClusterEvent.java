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
 * 集群事件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月18日 10:46
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月18日 10:46
 */
public class ClusterEvent {
    private final EventType eventType;
    private final String data;
    public ClusterEvent(EventType eventType, String data) {
        this.eventType = eventType;
        this.data = data;
    }
    public EventType getEventType() {
        return eventType;
    }


    public String getData() {
        return data;
    }


    public boolean isOnline() {
        return eventType == EventType.ONLINE;
    }

    public boolean isOffline() {
        return eventType == EventType.OFFLINE;
    }

    public boolean isDataChanged() {
        return eventType == EventType.DATA_CHANGED;
    }
}
