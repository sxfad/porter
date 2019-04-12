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
 * 数据权限控制表 实体Entity
 *
 * @author: FairyHood
 * @date: 2019-03-28 15:21:58
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-28 15:21:58
 */
public class DataAuthority implements Serializable {

    public DataAuthority() {

    }

    public DataAuthority(String objectTable, Long objectId, Integer ownerLevel, Long ownerId, Long operator,
            Integer type) {
        this.objectTable = objectTable;
        this.objectId = objectId;
        this.ownerLevel = ownerLevel;
        this.ownerId = ownerId;
        this.operator = operator;
        this.type = type;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 目标表.
     */
    private String objectTable;

    /**
     * 目标id.
     */
    private Long objectId;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjectTable() {
        return objectTable;
    }

    public void setObjectTable(String objectTable) {
        this.objectTable = objectTable;
    }

    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    public Integer getOwnerLevel() {
        return ownerLevel;
    }

    public void setOwnerLevel(Integer ownerLevel) {
        this.ownerLevel = ownerLevel;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getOperator() {
        return operator;
    }

    public void setOperator(Long operator) {
        this.operator = operator;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getIscancel() {
        return iscancel;
    }

    public void setIscancel(Integer iscancel) {
        this.iscancel = iscancel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
