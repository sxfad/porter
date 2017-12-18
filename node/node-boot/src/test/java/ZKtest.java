/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月17日 14:51
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.List;

/**
 * TODO
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月17日 14:51
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月17日 14:51
 */
public class ZKtest {
    public static  void main(String[] args) throws IOException, InterruptedException {
        (new Thread(){
            @Override
            public void run() {
                try {
                    ZooKeeper zk = new ZooKeeper("127.0.0.1:2181", 10000, new Watcher() {
                        @Override
                        public void process(WatchedEvent event) {
                            System.out.println(event);
                        }
                    });
                    List<String> children = zk.getChildren("/datas",false);
                    zk.exists("/datas",true);
                    zk.exists("/datas/999",true);
                    zk.exists("/datas/999",true);
                    zk.getChildren("/datas",true);
                    zk.getData("/datas/2", true, new Stat());
                   // zk.create("/tmp","{ts:1111111}".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (KeeperException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (true) {
            Thread.sleep(1000L);
        }
    }
}
