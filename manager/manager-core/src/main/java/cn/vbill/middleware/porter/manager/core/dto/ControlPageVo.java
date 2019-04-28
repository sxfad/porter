package cn.vbill.middleware.porter.manager.core.dto;

import cn.vbill.middleware.porter.manager.core.entity.DicControlTypePlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 权限设置页面数据组装
 *
 * @author: murasakiseifu
 * @date: 2019-04-26 09:56}
 * @version: V1.0
 * @review: murasakiseifu/2019-04-26 09:56}
 */
public class ControlPageVo {

    public ControlPageVo(OwnerVo owner, List<OwnerVo> shareOwner, Map<String, Object> dictControlType, List<DicControlTypePlugin> dicControlTypePlugins) {
        this.owner = owner;
        this.shareOwner = shareOwner;
        this.dictControlType = dictControlType;
        this.dicControlTypePlugins = dicControlTypePlugins;
    }

    /**
     * 权限所有人
     */
    private OwnerVo owner;

    /**
     * 权限共享者
     */
    private List<OwnerVo> shareOwner = new ArrayList<>();

    /**
     * 权限操作枚举
     */
    private Map<String, Object> dictControlType;

    /**
     * 权限操作按钮字典
     */
    private List<DicControlTypePlugin> dicControlTypePlugins = new ArrayList<>();


    /**
     * 权限所有人
     */
    public OwnerVo getOwner() {
        return owner;
    }

    /**
     * 权限所有人
     */
    public void setOwner(OwnerVo owner) {
        this.owner = owner;
    }

    /**
     * 权限共享者
     */
    public List<OwnerVo> getShareOwner() {
        return shareOwner;
    }

    /**
     * 权限共享者
     */
    public void setShareOwner(List<OwnerVo> shareOwner) {
        this.shareOwner = shareOwner;
    }

    /**
     * 权限操作枚举
     */
    public Map<String, Object> getDictControlType() {
        return dictControlType;
    }

    /**
     * 权限操作枚举
     */
    public void setDictControlType(Map<String, Object> dictControlType) {
        this.dictControlType = dictControlType;
    }

    /**
     * 权限操作按钮字典
     */
    public List<DicControlTypePlugin> getDicControlTypePlugins() {
        return dicControlTypePlugins;
    }

    /**
     * 权限操作按钮字典
     */
    public void setDicControlTypePlugins(List<DicControlTypePlugin> dicControlTypePlugins) {
        this.dicControlTypePlugins = dicControlTypePlugins;
    }
}
