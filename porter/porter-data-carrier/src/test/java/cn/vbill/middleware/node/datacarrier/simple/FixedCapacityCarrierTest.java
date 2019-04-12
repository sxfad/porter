/*
 * Copyright ©2018 vbill.cn.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
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
@SuppressWarnings("unchecked")
public class FixedCapacityCarrierTest {
    private final DataMapCarrier<String, String> carrier = new FixedCapacityCarrier(3);

    @Test
    public void push() throws InterruptedException {
        carrier.push("a", "1");
        carrier.printState();
    }
    @Test
    public void pull() throws InterruptedException {
        carrier.push("a", "1");
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
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
                carrier.pull("0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            carrier.print();
            latch.countDown();
        })).start();
        latch.await();
        carrier.print();
    }
}
