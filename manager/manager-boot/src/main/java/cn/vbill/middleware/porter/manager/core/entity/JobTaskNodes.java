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

/**
 * 任务节点分发表 实体Entity
 *
 * @author: FairyHood
 * @date: 2018-04-16 14:12:10
 * @version: V1.0-auto
 * @review: FairyHood/2018-04-16 14:12:10
 */
public class JobTaskNodes implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键.
     */
    private Long id;

    /**
     * 任务id.
     */
    private Long jobTaskId;

    /**
     * 节点id.
     */
    private String nodeId;

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
    public Long getJobTaskId() {
        return jobTaskId;
    }

    /**
     * 任务id set方法.
     */
    public void setJobTaskId(Long jobTaskId) {
        this.jobTaskId = jobTaskId;
    }

    /**
     * 节点id get方法.
     */
    public String getNodeId() {
        return nodeId == null ? null : nodeId.trim();
    }

    /**
     * 节点id set方法.
     */
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId == null ? null : nodeId.trim();
    }

}
