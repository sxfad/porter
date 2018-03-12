/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.dic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 集群方案
 * 
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:42
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:42
 */
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ClusterPlugin {
    ZOOKEEPER("ZOOKEEPER", "zookeeper");

    @Getter
    private final String code;
    @Getter
    private final String name;

    public static final List<ClusterPlugin> PLUGINS = new ArrayList<ClusterPlugin>() {
        {
            add(ZOOKEEPER);
        }
    };

    public static final HashMap<String, Object> PLUGMAP = new HashMap<String, Object>() {
        {
            put(ZOOKEEPER.code, ZOOKEEPER.name);
        }
    };
 
}
