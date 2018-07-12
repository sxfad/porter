/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 23:38
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.config.source;

import cn.vbill.middleware.porter.common.config.SourceConfig;
import cn.vbill.middleware.porter.common.dic.SourceType;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月04日 23:38
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月04日 23:38
 */
public class NameSourceConfig extends SourceConfig {
    @Setter @Getter private String sourceName;

    public NameSourceConfig() {
        sourceType =  SourceType.NAME_SOURCE;
    }

    @Override
    protected void childStuff() {
    }

    @Override
    protected String[] childStuffColumns() {
        return new String[0];
    }


    @Override
    protected boolean doCheck() {
        return true;
    }
}
