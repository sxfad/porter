/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package com.suixingpay.datas.common.cluster.data;

import com.suixingpay.datas.common.util.MachineUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

/**
 * 任务资源锁定
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月20日 13:45
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月20日 13:45
 */

@NoArgsConstructor
public class DTaskLock extends DObject {
    @Setter @Getter private String taskId;
    @Setter @Getter private String nodeId;
    @Setter @Getter private String resourceId;
    @Setter @Getter private String address = MachineUtils.IP_ADDRESS;
    @Setter @Getter private String hostName = MachineUtils.HOST_NAME;
    @Setter @Getter private String processId = MachineUtils.CURRENT_JVM_PID + "";

    public DTaskLock(String taskId, String nodeId, String resourceId) {
        this.taskId = taskId;
        this.nodeId = nodeId;
        this.resourceId = resourceId;
    }

    @Override
    public <T> void merge(T data) {
        DTaskLock lock = (DTaskLock) data;
        if (taskId.equals(lock.getTaskId()) && lock.getResourceId().equals(resourceId)) {
            if (!StringUtils.isBlank(lock.nodeId)) this.nodeId = lock.nodeId;
            if (!StringUtils.isBlank(lock.address)) this.address = lock.address;
            if (!StringUtils.isBlank(lock.hostName)) this.hostName = lock.hostName;
            if (!StringUtils.isBlank(lock.processId)) this.processId = lock.processId;
        }
    }
}
