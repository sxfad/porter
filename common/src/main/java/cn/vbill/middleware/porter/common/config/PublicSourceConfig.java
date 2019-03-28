/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月28日 10:55
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月28日 10:55
 * @version: V1.0
 * @review: zkevin/2019年03月28日 10:55
 */
public class PublicSourceConfig {
    @Setter
    @Getter
    private String code;

    @Setter
    @Getter
    private String nodeId;

    //是否启用数据源
    @Getter
    @Setter
    private boolean isUsing = true;


    @Getter
    @Setter
    private Map<String, String> source;
}
