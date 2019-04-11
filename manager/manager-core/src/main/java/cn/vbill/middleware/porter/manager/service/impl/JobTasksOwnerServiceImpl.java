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

import cn.vbill.middleware.porter.manager.core.dto.ControlSettingVo;
import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.core.entity.JobTasksOwner;
import cn.vbill.middleware.porter.manager.core.mapper.JobTasksOwnerMapper;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.rcc.RoleCheckContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTasksOwnerServiceImpl.class);

    @Autowired
    private JobTasksOwnerMapper jobTasksOwnerMapper;

    @Autowired
    private CUserService cUserService;

    @Override
    public Map<Integer, List<CUser>> jobOwnerTypeAll(Long jobId) {
        Integer ownerType = 1;
        Integer shareType = 2;
        // ownerType=1:任务所有者
        List<CUser> userOwner = cUserService.selectOwnersByJobId(jobId, ownerType);
        // shareType=2:任务共享者
        List<CUser> userShares = cUserService.selectOwnersByJobId(jobId, shareType);
        Map<Integer, List<CUser>> map = new HashMap<>();
        map.put(ownerType, userOwner);
        map.put(shareType, userShares);
        return map;
    }

    @Override
    public Integer findOwnerTypeByJobId(Long jobId) {
        Integer type = null;
        Boolean isManager = checkLoginRole();
        // 判断当前角色是否为管理员
        if (isManager) {
            type = 0;
            return type;
        }
        type = jobTasksOwnerMapper.findOwnerTypeByJobIdAndUserId(jobId, RoleCheckContext.getUserIdHolder().getUserId());
        return type;
    }

    @Override
    public void insertByJobTasks(Long jobId) {
        JobTasksOwner jobTasksOwner = new JobTasksOwner();
        jobTasksOwner.setJobId(jobId);
        jobTasksOwner.setOwnerId(RoleCheckContext.getUserIdHolder().getUserId());
        jobTasksOwnerMapper.insert(jobTasksOwner);
    }

    @Override
    public Integer jobOwnerSetting(ControlSettingVo controlSettingVo) {
        String controlType = null;
        if (null != controlSettingVo.getControlTypeEnum()) {
            controlType = controlSettingVo.getControlTypeEnum().getCode();
        }
        switch (controlType) {
            // 移交
            case "CHANGE":
                Integer changeNum = jobTasksOwnerMapper.delete(controlSettingVo.getJobId(), 1, null);
                if (!controlSettingVo.getToUserIds().isEmpty()) {
                    jobTasksOwnerMapper.batchInsert(controlSettingVo.getToUserIds(), controlSettingVo.getJobId(), 1);
                }
                return changeNum;
            // 共享
            case "SHARE":
                Integer shareNum = jobTasksOwnerMapper.delete(controlSettingVo.getJobId(), 2, null);
                if (!controlSettingVo.getToUserIds().isEmpty()) {
                    jobTasksOwnerMapper.batchInsert(controlSettingVo.getToUserIds(), controlSettingVo.getJobId(), 2);
                }
                return shareNum;
            // 作废
            case "CANCEL":
                Integer type = jobTasksOwnerMapper.findOwnerTypeByJobIdAndUserId(controlSettingVo.getJobId(), RoleCheckContext.getUserIdHolder().getUserId());
                return jobTasksOwnerMapper.delete(controlSettingVo.getJobId(), type, RoleCheckContext.getUserIdHolder().getUserId());
            // 回收
            case "RECYCLE":
                return jobTasksOwnerMapper.delete(controlSettingVo.getJobId(), null, null);
            default:
                LOGGER.error("ControlType为null!!");
                return null;
        }
    }

    /**
     * 判断当前登录用户是否为管理员
     *
     * @author MurasakiSeiFu
     * @date 2019-04-04 13:48
     * @param: []
     * @return: java.lang.Boolean
     */
    private Boolean checkLoginRole() {
        String roleCode = RoleCheckContext.getUserIdHolder().getRoleCode();
        return ("A0001".equals(roleCode) || "A0002".equals(roleCode));
    }
}