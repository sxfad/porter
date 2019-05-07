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
package cn.vbill.middleware.porter.manager.core.mapper;

import cn.vbill.middleware.porter.manager.core.entity.NodesOwner;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 节点所有权控制表 Mapper接口
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
public interface NodesOwnerMapper {

    /**
     * 根据节点id和用户id查询该用户type
     *
     * @param nodeId
     * @param userId
     * @return
     */
    Integer findOwnerTypeByNodeIdAndUserId(@Param("nodeId") Long nodeId, @Param("userId") Long userId);

    /**
     * 新增
     *
     * @param nodesOwner
     */
    Integer insert(NodesOwner nodesOwner);

    /**
     * 逻辑删除
     *
     * @param nodeId
     * @param type
     * @param userId
     * @return
     */
    Integer delete(@Param("nodeId") Long nodeId, @Param("type") Integer type, @Param("userId") Long userId);

    /**
     * 批量新增
     *
     * @param toUserIds
     * @param nodeId
     * @param type
     */
    void batchInsert(@Param("toUserIds") List<Long> toUserIds, @Param("nodeId") Long nodeId, @Param("type") Integer type);
}