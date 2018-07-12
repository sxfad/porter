/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月13日 14:53
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.core.loader;

import cn.vbill.middleware.porter.core.event.s.EventType;
import cn.vbill.middleware.porter.common.consumer.Position;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 单条数据执行结果，用于提交任务进度及性能指标统计
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月13日 14:53
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月13日 14:53
 */

@AllArgsConstructor
public class SubmitStatObject {
    @Setter @Getter private String schema;
    @Setter @Getter private String table;
    @Setter @Getter private EventType type;
    @Setter @Getter private int affect;
    @Setter @Getter private Position position;
    @Setter @Getter private Date opTime;
}
