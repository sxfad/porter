/**
 * 
 */
package cn.vbill.middleware.porter.manager.cluster.zookeeper;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public interface ZKClusterLockService {

    void createLock(String lockName);
    
    void deleteLock(String lockName);
}
