/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.statistics;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月09日 16:16
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月09日 16:16
 */
public class TaskPerformance {
    @Setter @Getter private String taskId;
    @Setter @Getter private String resourceId;
    @Setter @Getter private String schema;
    @Setter @Getter private String table;
    @Setter @Getter private Long insertRow = 0L;
    @Setter @Getter private Long updateRow = 0L;
    @Setter @Getter private Long deleteRow = 0L;
    @Setter @Getter private Long errorUpdateRow = 0L;
    @Setter @Getter private Long errorInsertRow = 0L;
    @Setter @Getter private Long errorDeleteRow = 0L;
    //告警次数
    @Setter @Getter private  Long alertedTimes = 0L;
    @JSONField(format = "yyyyMMddHHmmss")
    @Setter @Getter private Date time;
}
