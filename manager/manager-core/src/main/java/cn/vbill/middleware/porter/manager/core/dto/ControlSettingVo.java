package cn.vbill.middleware.porter.manager.core.dto;

import cn.vbill.middleware.porter.manager.core.enums.ControlTypeEnum;

import java.util.List;

/**
 * 任务权限设置VO
 *
 * @author: murasakiseifu
 * @date: 2019-04-04 10:00
 * @version: V1.0
 * @review: murasakiseifu/2019-04-04 10:00}
 */
public class ControlSettingVo {

    /**
     * 任务id
     */
    private Long jobId;

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 任务权限操作类型枚举
     */
    private ControlTypeEnum controlTypeEnum;

    /**
     * 归属用户Id
     */
    private List<Long> toUserIds;


    /**
     * 任务id.get
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 任务id.set
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 任务权限操作类型枚举.get
     */
    public ControlTypeEnum getControlTypeEnum() {
        return controlTypeEnum;
    }

    /**
     * 任务权限操作类型枚举.set
     */
    public void setControlTypeEnum(ControlTypeEnum controlTypeEnum) {
        this.controlTypeEnum = controlTypeEnum;
    }

    /**
     * 归属用户Id.get
     */
    public List<Long> getToUserIds() {
        return toUserIds;
    }

    /**
     * 归属用户Id.set
     */
    public void setToUserIds(List<Long> toUserIds) {
        this.toUserIds = toUserIds;
    }

    /**
     * 节点id.get
     */
    public String getNodeId() {
        return nodeId;
    }

    /**
     * 节点id.set
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
