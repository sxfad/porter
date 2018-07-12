/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月02日 15:24
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.cluster.data;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年01月02日 15:24
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年01月02日 15:24
 */
public interface DCallback {
    default void callback(DObject object) {
        return;
    }
    default void callback(String data) {
        return;
    }
    default void callback(List<DObject> objects) {
        return;
    }
}
