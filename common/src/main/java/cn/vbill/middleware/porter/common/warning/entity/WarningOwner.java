/**
 * 
 */
package cn.vbill.middleware.porter.common.warning.entity;

import java.util.ArrayList;
import java.util.List;

import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class WarningOwner {

    public WarningOwner() {

    }

    /** 权限所有人. */
    private WarningReceiver owner;

    /** 权限共享者 . */
    private List<WarningReceiver> shareOwner = new ArrayList<>();

    public WarningReceiver getOwner() {
        return owner;
    }

    public void setOwner(WarningReceiver owner) {
        this.owner = owner;
    }

    public List<WarningReceiver> getShareOwner() {
        return shareOwner;
    }

    public void setShareOwner(List<WarningReceiver> shareOwner) {
        this.shareOwner = shareOwner;
    }
}
