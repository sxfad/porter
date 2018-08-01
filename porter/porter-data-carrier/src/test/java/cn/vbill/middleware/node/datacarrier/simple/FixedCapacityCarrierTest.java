/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 11:01
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package cn.vbill.middleware.node.datacarrier.simple;

import cn.vbill.middleware.porter.datacarrier.DataMapCarrier;
import cn.vbill.middleware.porter.datacarrier.simple.FixedCapacityCarrier;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年05月02日 11:01
 * @version: V1.0
 * @review: zkevin/2018年05月02日 11:01
 */
public class FixedCapacityCarrierTest {
    private final DataMapCarrier<String, String> carrier = new FixedCapacityCarrier(3);

    @Test
    public void push() throws InterruptedException {
        carrier.push("a", "1");
        carrier.printState();
    }
    @Test
    public void pull() {
        String value = carrier.pull("a");
        System.out.println("key : a" + ", value : " + value);
        carrier.printState();
    }
    @Test
    public void containsKey() {
        boolean has = carrier.containsKey("a");
        System.out.println("key : a, has : " + has);
        carrier.printState();
    }

    @Test
    public void unionTest() throws InterruptedException {
        carrier.push("a", "1");
        carrier.printState();
        carrier.push("b", "2");
        carrier.push("c", "3");
        carrier.printState();
        carrier.pull("c");
        carrier.printState();
        System.out.println("key : c, has : " + carrier.containsKey("c"));
    }

    @Ignore
    @Test
    public void unionThreadTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        for (int i = 0; i < 4; i++) {
            int finalI = i;
            (new Thread(() -> {
                try {
                    carrier.push(finalI + "", finalI + "");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                carrier.print();
                latch.countDown();
            })).start();
        }

        (new Thread(() -> {
            try {
                Thread.currentThread().sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            carrier.pull("0");
            carrier.print();
            latch.countDown();
        })).start();
        latch.await();
        carrier.print();
    }
}
