/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:02
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.db.meta;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月06日 16:02
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月06日 16:02
 */
public class TableColumn {
    @Getter @Setter private String name;
    @Getter @Setter private String defaultValue;
    @Getter @Setter private boolean required;
    @Getter @Setter private boolean primaryKey;
    //
    /**
     * 不同的目标类型有不同的表示
     * jdbc : java.sql.Types
     * kudu : org.apache.kudu.Type
     */
    @Getter @Setter private int typeCode;
}
