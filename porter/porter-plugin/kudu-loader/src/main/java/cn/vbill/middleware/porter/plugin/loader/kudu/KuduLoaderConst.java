/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月19日 11:22
 * @Copyright ©2019 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.plugin.loader.kudu;

import lombok.Getter;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月19日 11:22
 * @version: V1.0
 * @review: zkevin/2019年03月19日 11:22
 */
public enum KuduLoaderConst {
    LOADER_SOURCE_TYPE_NAME("KUDU"),
    LOADER_PLUGIN_NAME("KUDU_NATIVE");
    @Getter
    private String code;
    KuduLoaderConst(String code) {
        this.code = code;
    }
}
