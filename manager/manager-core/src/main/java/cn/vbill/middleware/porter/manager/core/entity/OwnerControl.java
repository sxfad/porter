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

/**
 * 权限控制操作类型表 实体Entity
 *
 * @author: FairyHood
 * @date: 2019-04-02 10:58:29
 * @version: V1.0-auto
 * @review: FairyHood/2019-04-02 10:58:29
 */
public class OwnerControl implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 所有者权限类型(1:所有者 2.共享者 0:管理员).
     */
    private Integer ownerType;

    /**
     * 操作类型id.
     */
    private Long controlTypeId;


    /**
     * 主键.get
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键.set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 所有者权限类型(1:所有者 2.共享者 0:管理员).get
     */
    public Integer getOwnerType() {
        return ownerType;
    }

    /**
     * 所有者权限类型(1:所有者 2.共享者 0:管理员).set
     */
    public void setOwnerType(Integer ownerType) {
        this.ownerType = ownerType;
    }

    /**
     * 操作类型id.get
     */
    public Long getControlTypeId() {
        return controlTypeId;
    }

    /**
     * 操作类型id.set
     */
    public void setControlTypeId(Long controlTypeId) {
        this.controlTypeId = controlTypeId;
    }
}
