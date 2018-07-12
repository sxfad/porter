/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 10:49
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.task.extract;

import cn.vbill.middleware.porter.core.event.s.EventProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月09日 10:49
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月09日 10:49
 */

@AllArgsConstructor
public class ExtractMetadata {
    @Getter private final List<String> excludeTables;
    @Getter private final List<String> includeTables;
    @Getter private final EventProcessor processor;
}
