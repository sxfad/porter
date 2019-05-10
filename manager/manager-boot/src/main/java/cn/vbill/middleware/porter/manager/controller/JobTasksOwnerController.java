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
package cn.vbill.middleware.porter.manager.controller;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.manager.core.dto.ControlPageVo;
import cn.vbill.middleware.porter.manager.core.dto.ControlSettingVo;
import cn.vbill.middleware.porter.manager.service.CUserService;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

/**
 * 任务所有权控制表 controller控制器
 *
 * @author: FairyHood
 * @date: 2019-03-20 16:29:06
 * @version: V1.0-auto
 * @review: FairyHood/2019-03-20 16:29:06
 */
@Api(description = "任务所有权控制表管理")
@RestController
@RequestMapping("/manager/jobtasksowner")
public class JobTasksOwnerController {

    private Logger log = LoggerFactory.getLogger(JobTasksOwnerController.class);

    @Autowired
    protected JobTasksOwnerService jobTasksOwnerService;

    @Autowired
    private CUserService cuserService;

    /**
     * 权限设置页面数据
     *
     * @author MurasakiSeiFu
     * @date 2019-04-26 10:09
     * @param: [jobId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/setPage/{jobId}")
    @ApiOperation(value = "权限设置页面数据组", notes = "权限设置页面数据组")
    public ResponseMessage jobOwnerPage(@PathVariable("jobId") Long jobId) {
        ControlPageVo controlPageVo = jobTasksOwnerService.makeControlPage(jobId);
        return ok(controlPageVo);
    }

    /**
     * 权限设置
     *
     * @author MurasakiSeiFu
     * @date 2019-04-04 10:05
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @PutMapping
    @ApiOperation(value = "权限设置", notes = "权限设置")
    public ResponseMessage jobOwnerSetting(@RequestBody ControlSettingVo controlSettingVo) {
        Integer number = jobTasksOwnerService.jobOwnerSetting(controlSettingVo);
        Long id = controlSettingVo.getJobId();
        // 权限信息(邮箱数据上传)
        try {
            WarningOwner warningOwner = cuserService.selectJobWarningOwner(id);
            ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                String nodePath = AbstractClusterListener.BASE_CATALOG + "/task/" + id + "/owner";
                if (!StringUtils.isBlank(nodePath)) {
                    client.changeData(nodePath, false, false, JSON.toJSONString(warningOwner));
                }
            });
            log.info("zk变更任务id[{}]权限数据到zk成功,详细信息[{}]!", id, JSON.toJSONString(warningOwner));
        } catch (Exception e) {
            log.error("zk变更任务id[{}]权限数据到zk失败,请关注！", id, e);
            return ResponseMessage.error("变更任务权限失败，请关注！");
        }
        return ok(number);
    }

    /**
     * 回显所有者、共享者
     *
     * @author MurasakiSeiFu
     * @date 2019-05-09 09:08
     * @param: [jobId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/findJobOwner/{jobId}")
    @ApiOperation(value = "回显所有者、共享者", notes = "回显所有者、共享者")
    public ResponseMessage findOwnerByJobId(@PathVariable("jobId") Long jobId) {
        ControlPageVo controlPageVo = jobTasksOwnerService.findOwnerByJobId(jobId);
        return ok(controlPageVo);
    }

}