/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:08
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.common.db;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年03月01日 16:08
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年03月01日 16:08
 */
public enum  SqlErrorCode {
    //ORA-00904: "C3": 标识符无效
    ERROR_904(904),
    //ORA-00942: 表或视图不存在
    ERROR_942(942),
    //HY000: Field 'c3' doesn't have a default value
    ERROR_1364(1364),
    //ORA-01438: 值大于为此列指定的允许精度
    ERROR_1438(1438),
    //字段长度与插入内容不符
    ERROR_12899(12899),
    //ORA-01400: cannot insert NULL into
    ERROR_1400(1400),
    //errorCode 17002, state 08006
    //IO Error: The Network Adapter could not establish the connection
    ERROR_17002(17002);

    public final int code;
    SqlErrorCode(int code) {
        this.code = code;
    }
}
