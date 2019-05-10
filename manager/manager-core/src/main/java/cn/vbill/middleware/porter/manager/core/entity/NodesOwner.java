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
     * 节点id.
     */
    private String nodeId;

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
     * 类型(1：权限所有人 2：权限共享者).
     */
    private Integer type;

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
    public String getNodeId() {
        return nodeId;
    }

    /**
     * 任务id set方法.
     */
    public void setNodeId(String nodeId) {
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
     * 类型(1：权限所有人 2：权限共享者).get
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 类型(1：权限所有人 2：权限共享者).set
     */
    public Integer getType() {
        return type;
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
