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

import java.util.ArrayList;
import java.util.List;

import cn.vbill.middleware.porter.manager.core.enums.AuthorityBtnEnum;

/**
 * 权限组件
 * 
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class DataAuthorityVo {

    public DataAuthorityVo() {

    }

    public DataAuthorityVo(Long userId, OwnerVo owner, List<OwnerVo> shareOwner, List<AuthorityBtnEnum> btns) {
        this.userId = userId;
        this.owner = owner;
        this.shareOwner = shareOwner;
        this.btns = btns;
    }

    private Long userId;

    /** 权限所有人. */
    private OwnerVo owner;

    /** 权限共享者 . */
    private List<OwnerVo> shareOwner = new ArrayList<>();

    /** 权限按钮 . */
    private List<AuthorityBtnEnum> btns = new ArrayList<>();

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OwnerVo getOwner() {
        return owner;
    }

    public void setOwner(OwnerVo owner) {
        this.owner = owner;
    }

    public List<AuthorityBtnEnum> getBtns() {
        return btns;
    }

    public void setBtns(List<AuthorityBtnEnum> btns) {
        this.btns = btns;
    }

    public List<OwnerVo> getShareOwner() {
        return shareOwner;
    }

    public void setShareOwner(List<OwnerVo> shareOwner) {
        this.shareOwner = shareOwner;
    }

}
