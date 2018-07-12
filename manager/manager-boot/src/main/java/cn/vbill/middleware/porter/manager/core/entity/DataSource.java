package cn.vbill.middleware.porter.manager.core.entity;

import cn.vbill.middleware.porter.common.dic.SourceType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 数据源信息表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-03-07 13:40:30
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 13:40:30
 */
public class DataSource implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 数据源名称.
     */
    @NotNull(message = "数据源名称不能为空")
    private String name;

    /**
     * 数据源类型.
     */
    private SourceType dataType;

    /**
     * 创建人.
     */
    private Long creater;

    /**
     * 创建时间.
     */
    private Date createTime;

    /**
     * 状态.
     */
    private Integer state;

    /**
     * 是否作废.
     */
    private Integer iscancel;

    /**
     * 备注.
     */
    private String remark;

    /**
     * 数据源信息关联表list
     */
    private List<DataSourcePlugin> plugins = new ArrayList<>();

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
     * 数据源名称 get方法.
     */
    public String getName() {
        return name == null ? null : name.trim();
    }

    /**
     * 数据源名称 set方法.
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 数据源类型 get方法.
     */
    public SourceType getDataType() {
        return dataType;
    }

    /**
     * 数据源类型 set方法.
     */
    public void setDataType(SourceType dataType) {
        this.dataType = dataType;
    }

    /**
     * 创建人 get方法.
     */
    public Long getCreater() {
        return creater;
    }

    /**
     * 创建人 set方法.
     */
    public void setCreater(Long creater) {
        this.creater = creater;
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

    public List<DataSourcePlugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(List<DataSourcePlugin> plugins) {
        this.plugins = plugins;
    }
}
