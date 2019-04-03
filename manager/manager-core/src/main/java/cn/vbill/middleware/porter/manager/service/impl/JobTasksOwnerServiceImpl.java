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
package cn.vbill.middleware.porter.manager.service.impl;

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksOwnerMapper;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.page.Page;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务所有权控制表 服务实现类
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Service
public class JobTasksOwnerServiceImpl implements JobTasksOwnerService {

    @Autowired
    private JobTasksOwnerMapper jobTasksOwnerMapper;

    @Autowired
    private CUserService cUserService;

    @Override
    public Map<Integer, List<CUser>> jobOwnerTypeAll(Long jobId) {
        List<CUser> userOwner = null;
        List<CUser> userShares = null;
        List<Long> userIdOnes = jobTasksOwnerMapper.selectOwnerIdByJobIdOrTypeOne(jobId, 1);
        List<Long> userIdTwos = jobTasksOwnerMapper.selectOwnerIdByJobIdOrTypeOne(jobId, 2);
//        if (!userIdOnes.isEmpty()) {
//            userOwner = cUserService.selectByIdList(userIdOnes);
//        }
//        if (!userIdTwos.isEmpty()) {
//            userShares = cUserService.selectByIdList(userIdTwos);
//        }
//        Map<Integer, List<CUser>> map = new HashMap<>();
//        map.put(1, userOwner);
//        map.put(2, userShares);
        return null;
    }

    @Override
    public Integer insert(JobTasksOwner jobTasksOwner) {
        return jobTasksOwnerMapper.insert(jobTasksOwner);
    }

    @Override
    public Integer update(Long id, JobTasksOwner jobTasksOwner) {
        return jobTasksOwnerMapper.update(id, jobTasksOwner);
    }

    @Override
    public Integer delete(Long id) {
        return jobTasksOwnerMapper.delete(id);
    }

    @Override
    public JobTasksOwner selectById(Long id) {
        return jobTasksOwnerMapper.selectById(id);
    }

    @Override
    public Page<JobTasksOwner> page(Page<JobTasksOwner> page) {
        Integer total = jobTasksOwnerMapper.pageAll(1);
        if (total > 0) {
            page.setTotalItems(total);
            page.setResult(jobTasksOwnerMapper.page(page, 1));
        }
        return page;
    }

    @Override
    public void insertByJobTasks(Long jobId) {
        JobTasksOwner jobTasksOwner = new JobTasksOwner();
        jobTasksOwner.setJobId(jobId);
        jobTasksOwner.setOwnerId(RoleCheckContext.getUserIdHolder().getUserId());
        jobTasksOwnerMapper.insert(jobTasksOwner);
    }

    @Override
    @Transactional
    public Integer changePermission(Long jobId, Long fromUserId, Long toUserId) {
        // 是否为非"A9999"权限用户操作
        if (null != fromUserId) {
            // 1.删除当前用户控制权
            jobTasksOwnerMapper.deleteByOwnerIdAndJobId(jobId, fromUserId);
        } else {
            jobTasksOwnerMapper.deleteByOwnerIdAndJobId(jobId, RoleCheckContext.getUserIdHolder().getUserId());
        }
        // 2.移交控制权给toUserId
        return jobTasksOwnerMapper.changePermission(jobId, toUserId);
    }

    @Override
    public Integer sharePermission(Long jobId, List<CUser> toUserIds) {
        return jobTasksOwnerMapper.sharePermission(jobId, toUserIds);
    }
}