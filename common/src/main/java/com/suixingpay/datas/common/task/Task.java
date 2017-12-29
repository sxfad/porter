/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.task;

import com.suixingpay.datas.common.datasource.DataDriverType;
import com.suixingpay.datas.common.datasource.NamedDataDriver;
import com.suixingpay.datas.common.datasource.meta.KafkaDriverMeta;
import com.suixingpay.datas.common.db.TableMapper;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月19日 10:12
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月19日 10:12
 */
public class Task {
    //manager下发的任务ID
    private String taskId;
    //源数据库地址
    private NamedDataDriver sourceDriver;
    //目标数据库地址
    private NamedDataDriver targetDriver;
    //sql事件源地址
    private NamedDataDriver dataDriver;
    //自定义字段映射
    private List<TableMapper> mappers;
    public String[] listTopic() {
        String[] topics = null;
        if (DataDriverType.KAFKA == dataDriver.getType()) {
            KafkaDriverMeta meta = (KafkaDriverMeta) DataDriverType.KAFKA.getMeta();
            topics = dataDriver.getExtendAttr().getOrDefault(meta.TOPIC, "").split(",");
        }
        return null == topics ? new String[]{} :topics;
    }
    public NamedDataDriver getSourceDriver() {
        return sourceDriver;
    }

    public void setSourceDriver(NamedDataDriver sourceDriver) {
        this.sourceDriver = sourceDriver;
    }

    public NamedDataDriver getTargetDriver() {
        return targetDriver;
    }

    public void setTargetDriver(NamedDataDriver targetDriver) {
        this.targetDriver = targetDriver;
    }

    public NamedDataDriver getDataDriver() {
        return dataDriver;
    }

    public void setDataDriver(NamedDataDriver dataDriver) {
        this.dataDriver = dataDriver;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<TableMapper> getMappers() {
        return mappers;
    }

    public void setMappers(List<TableMapper> mappers) {
        this.mappers = mappers;
    }
}
