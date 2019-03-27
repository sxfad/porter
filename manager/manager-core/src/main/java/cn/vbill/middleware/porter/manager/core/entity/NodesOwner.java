/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */
package cn.vbill.middleware.porter.manager.core.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 节点所有权控制表 实体Entity
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public class NodesOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private Long nodeId;

    /**
     * 权限控制类型(1:人2:部门3:角色组).
     */
    private Integer ownerLevel;

    /**
     * 所有者id.
     */
    private Long ownerId;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 修改时间.
     */
    private Date updateTime;

    /**
     * 操作人.
     */
    private Long operator;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 主键 get方法.
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键 set方法.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 任务id get方法.
     */
    public Long getNodeId() {
        return nodeId;
    }

    /**
     * 任务id set方法.
     */
    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    /**
     * 权限控制类型(1:人2:部门3:角色组) get方法.
     */
    public Integer getOwnerLevel() {
        return ownerLevel;
    }

    /**
     * 权限控制类型(1:人2:部门3:角色组) set方法.
     */
    public void setOwnerLevel(Integer ownerLevel) {
        this.ownerLevel = ownerLevel;
    }

    /**
     * 所有者id get方法.
     */
    public Long getOwnerId() {
        return ownerId;
    }

    /**
     * 所有者id set方法.
     */
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    /**
     * 创建时间 get方法.
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间 set方法.
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间 get方法.
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间 set方法.
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 操作人 get方法.
     */
    public Long getOperator() {
        return operator;
    }

    /**
     * 操作人 set方法.
     */
    public void setOperator(Long operator) {
        this.operator = operator;
    }

    /**
     * 是否作废 get方法.
     */
    public Integer getIscancel() {
        return iscancel;
    }

    /**
     * 是否作废 set方法.
     */
    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    /**
     * 备注 get方法.
     */
    public String getRemark() {
        return remark == null ? null : remark.trim();
    }

    /**
     * 备注 set方法.
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}
