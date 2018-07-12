/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.statistics;

import cn.vbill.middleware.porter.common.cluster.data.DTaskStat;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 任务每秒统计信息
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class TaskPerformance extends StatisticData {
    @JSONField(serialize = false, deserialize = false)
    private static final String NAME = "task";
    //任务ID
    @Setter @Getter private String taskId;
    //泳道ID
    @Setter @Getter private String swimlaneId;
    //表schema
    @Setter @Getter private String schema;
    //表名
    @Setter @Getter private String table;
    //插入行数
    @Setter @Getter private Long insertRow = 0L;
    //更新行数
    @Setter @Getter private Long updateRow = 0L;
    //删除行数
    @Setter @Getter private Long deleteRow = 0L;
    //更新错误行数
    @Setter @Getter private Long errorUpdateRow = 0L;
    //插入失败行数
    @Setter @Getter private Long errorInsertRow = 0L;
    //删除失败行数
    @Setter @Getter private Long errorDeleteRow = 0L;
    //告警次数
    @Setter @Getter private  Long alertedTimes = 0L;
    //统计上报时间
    @JSONField(format = "yyyyMMddHHmm")
    @Setter @Getter private Date time;

    public TaskPerformance() {

    }

    public TaskPerformance(DTaskStat stat) {
        this();
        this.taskId = stat.getTaskId();
        this.swimlaneId = stat.getSwimlaneId();
        this.schema = stat.getSchema();
        this.table = stat.getTable();
        this.insertRow = stat.getInsertRow().get();
        this.updateRow = stat.getUpdateRow().get();
        this.deleteRow = stat.getDeleteRow().get();
        this.errorInsertRow = stat.getErrorInsertRow().get();
        this.errorUpdateRow = stat.getErrorUpdateRow().get();
        this.errorDeleteRow = stat.getErrorDeleteRow().get();
        this.alertedTimes = stat.getAlertedTimes().get();
        this.time = new Date();
    }

    @Override
    public String getCategory() {
        return NAME;
    }

    @Override
    protected String getSubId() {
        return taskId + "-" + swimlaneId;
    }
}
