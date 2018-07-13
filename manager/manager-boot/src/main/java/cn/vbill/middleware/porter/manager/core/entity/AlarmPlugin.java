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

package cn.vbill.middleware.porter.manager.core.entity;

/**
 * 告警配置策略内容表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-08 10:44:50
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-08 10:44:50
 */
public class AlarmPlugin implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 告警策略id.
     */
    private Long alarmId;

    /**
     * 告警方式.
     */
    private String alarmType;

    /**
     * 字段code.
     */
    private String pluginCode;

    /**
     * 字段名称.
     */
    private String pluginName;

    /**
     * 字段内容.
     */
    private String pluginValue;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 告警策略id get方法.
     */
    public Long getAlarmId() {
        return alarmId;
    }

    /**
     * 告警策略id set方法.
     */
    public void setAlarmId(Long alarmId) {
        this.alarmId = alarmId;
    }

    /**
     * 告警方式 get方法.
     */
    public String getAlarmType() {
        return alarmType == null ? null : alarmType.trim();
    }

    /**
     * 告警方式 set方法.
     */
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType == null ? null : alarmType.trim();
    }

    /**
     * 字段code get方法.
     */
    public String getPluginCode() {
        return pluginCode == null ? null : pluginCode.trim();
    }

    /**
     * 字段code set方法.
     */
    public void setPluginCode(String pluginCode) {
        this.pluginCode = pluginCode == null ? null : pluginCode.trim();
    }

    /**
     * 字段名称 get方法.
     */
    public String getPluginName() {
        return pluginName == null ? null : pluginName.trim();
    }

    /**
     * 字段名称 set方法.
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName == null ? null : pluginName.trim();
    }

    /**
     * 字段内容 get方法.
     */
    public String getPluginValue() {
        return pluginValue == null ? null : pluginValue.trim();
    }

    /**
     * 字段内容 set方法.
     */
    public void setPluginValue(String pluginValue) {
        this.pluginValue = pluginValue == null ? null : pluginValue.trim();
    }

}
