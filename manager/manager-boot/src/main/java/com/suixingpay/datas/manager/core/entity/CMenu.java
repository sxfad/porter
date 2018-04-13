package com.suixingpay.datas.manager.core.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单目录表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class CMenu implements java.io.Serializable {

    public CMenu() {

    }

    public CMenu(Integer isleaf) {
        this.isleaf = isleaf;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 编号.
     */
    private String code;

    /**
     * 父类编号.
     */
    private String fathercode;

    /**
     * 目录名称.
     */
    private String name;

    /**
     * 目录路径.
     */
    private String menuUrl;

    /** 图片 */
    private String menuImage;

    /**
     * 目录等级.
     */
    private Integer level;

    /**
     * 目录排序.
     */
    private Integer sort;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 类型.
     */
    private Integer type;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 是否叶子.
     */
    private Integer isleaf;

    private List<CMenu> menus = new ArrayList<>();

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
     * 编号 get方法.
     */
    public String getCode() {
        return code == null ? null : code.trim();
    }

    /**
     * 编号 set方法.
     */
    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    /**
     * 父类编号 get方法.
     */
    public String getFathercode() {
        return fathercode == null ? null : fathercode.trim();
    }

    /**
     * 父类编号 set方法.
     */
    public void setFathercode(String fathercode) {
        this.fathercode = fathercode == null ? null : fathercode.trim();
    }

    /**
     * 目录名称 get方法.
     */
    public String getName() {
        return name == null ? null : name.trim();
    }

    /**
     * 目录名称 set方法.
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 目录路径 get方法.
     */
    public String getMenuUrl() {
        return menuUrl == null ? null : menuUrl.trim();
    }

    /**
     * 目录路径 set方法.
     */
    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl == null ? null : menuUrl.trim();
    }

    /**
     * 目录等级 get方法.
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * 目录等级 set方法.
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * 目录排序 get方法.
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 目录排序 set方法.
     */
    public void setSort(Integer sort) {
        this.sort = sort;
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
     * 类型 get方法.
     */
    public Integer getType() {
        return type;
    }

    /**
     * 类型 set方法.
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * 状态 get方法.
     */
    public Integer getState() {
        return state;
    }

    /**
     * 状态 set方法.
     */
    public void setState(Integer state) {
        this.state = state;
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

    /**
     * 是否叶子 get方法
     */
    public Integer getIsleaf() {
        return isleaf;
    }

    /**
     * 是否叶子 set方法
     */
    public void setIsleaf(Integer isleaf) {
        this.isleaf = isleaf;
    }

    public List<CMenu> getMenus() {
        return menus;
    }

    public void setMenus(List<CMenu> menus) {
        this.menus = menus;
    }

    /** 图片 get方法 */
    public String getMenuImage() {
        return menuImage == null ? null : menuImage.trim();
    }

    /** 图片 set方法 */
    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage == null ? null : menuImage.trim();
    }
}
