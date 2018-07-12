/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:13
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.porter.datacarrier;

/**
 * map carrier
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 10:13
 * @version: V1.0
 * @review: zkevin/2018年05月02日 10:13
 */

public interface DataMapCarrier<K, V> extends DataContainer {
    boolean push(K key, V v) throws InterruptedException;
    V pull(K key);
    boolean containsKey(K key);
    void printState();
    void print();
}
