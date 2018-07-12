/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 19:07
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 19:07
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 19:07
 */
public class TableMapperConfig {

    public TableMapperConfig() {

    }

    public TableMapperConfig(String[] schema, String[] table, Map<String, String> column, Boolean ignoreTargetCase,
            Boolean forceMatched) {
        this.auto = false;
        this.schema = schema;
        this.table = table;
        this.column = column;
        this.ignoreTargetCase = ignoreTargetCase;
        this.forceMatched = forceMatched;
    }

    @Getter
    @Setter
    private boolean auto = true;

    @Getter
    @Setter
    private String[] schema;

    @Getter
    @Setter
    private String[] table;

    @Getter
    @Setter
    private String[] updateDate;

    @Getter
    @Setter
    private Map<String, String> column;
    // 忽略目标端大小写
    @Getter
    @Setter
    private boolean ignoreTargetCase = true;
    /**
     * 字段映射后，强制目标端字段和源端字段一致，否则任务抛出异常停止
     */
    @Getter
    @Setter
    private boolean forceMatched = true;
}
