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

package cn.vbill.middleware.porter.manager.core.dto;

import cn.vbill.middleware.porter.manager.core.entity.CUser;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class OwnerVo {

    public OwnerVo() {

    }

    public OwnerVo(CUser user, Integer authorityType) {
        if (user != null) {
            this.name = user.getNickname();
            this.email = user.getEmail();
            this.authorityType = authorityType;
        }
    }

    private String name;

    private String email;

    /** 权限类型 1 所有者 2共享者. */
    private Integer authorityType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAuthorityType() {
        return authorityType;
    }

    public void setAuthorityType(Integer authorityType) {
        this.authorityType = authorityType;
    }

}