/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.core.event.s;


import cn.vbill.middleware.porter.common.consumer.Position;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 消息事件
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月13日 18:22
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月13日 18:22
 */
public class MessageEvent {
    //行同步位点
    @Getter @Setter private Position rowPosition;
    //当前行所在批次位点
    @Getter @Setter private Position bucketPosition;
    @Getter @Setter private String schema;
    @Getter @Setter private String table;
    //操作类型 I U D T
    @Getter @Setter private EventType opType;
    //操作时间
    @Getter @Setter private Date opTs;
    //解析事件的时间
    @Getter @Setter private Date currentTs;
    @Getter @Setter private long consumerTime;
    //解析事件的时间
    @Getter @Setter private long consumedTime;
    //修改之后的值
    @Getter @Setter private Map<String, Object> after = new HashMap<>();
    //修改之前的值
    @Getter @Setter private Map<String, Object> before = new HashMap<>();
    @Getter @Setter private List<String> primaryKeys = new ArrayList<>();
}
