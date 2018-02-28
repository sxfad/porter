/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.node.core;

import com.suixingpay.datas.common.node.Node;
import com.suixingpay.datas.common.node.NodeStatusType;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月24日 16:03
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月24日 16:03
 */
public enum  NodeContext {
    INSTANCE();
    private final ReadWriteLock nodeLock = new ReentrantReadWriteLock();
    private ApplicationContext context;
    private final Node node = new Node();
    public <T> T getBean(Class<T> clazz) {
        return null != context ? context.getBean(clazz) : null;
    }

    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    public void syncNodeId(String nodeId) {
        try {
            nodeLock.writeLock().lock();
            node.setNodeId(nodeId);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public void syncNodeStatus(NodeStatusType status) {
        try {
            nodeLock.writeLock().lock();
            node.setStatus(status);
        } finally {
            nodeLock.writeLock().unlock();
        }
    }

    public String getNodeId() {
        try {
            nodeLock.readLock().lock();
            return node.getNodeId();
        } finally {
            nodeLock.readLock().unlock();
        }
    }

    public NodeStatusType getNodeStatus() {
        try {
            nodeLock.readLock().lock();
            return node.getStatus();
        } finally {
            nodeLock.readLock().unlock();
        }
    }
}
