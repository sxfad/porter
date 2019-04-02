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

import cn.vbill.middleware.porter.manager.core.entity.CUser;
import cn.vbill.middleware.porter.manager.service.JobTasksOwnerService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

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

    @Autowired
    protected JobTasksOwnerService jobTasksOwnerService;

    /**
     * 回显任务所有者、任务共享者
     *
     * @author MurasakiSeiFu
     * @date 2019-04-02 14:57
     * @param: [jobId]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/typeall/{jobId}")
    @ApiOperation(value = "回显任务所有者、任务共享者")
    public ResponseMessage jobOwnerTypeAll(@PathVariable("jobId") Long jobId) {
        Map<Integer, List<CUser>> map = jobTasksOwnerService.jobOwnerTypeAll(jobId);
        return ok(map);
    }

//    /**
//     * 权限移交
//     *
//     * @author FuZizheng
//     * @date 2019-03-26 15:52
//     * @param: [jobId,
//     *             fromUserId, toUserId]
//     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
//     */
//    @PostMapping("/change")
//    @ApiOperation(value = "权限移交", notes = "权限移交")
//    public ResponseMessage changePermission(@RequestParam(required = true) Long jobId,
//            @RequestParam(required = false) Long fromUserId, @RequestParam(required = true) Long toUserId) {
//        Integer number = jobTasksOwnerService.changePermission(jobId, fromUserId, toUserId);
//        return ok(number);
//    }
//
//    /**
//     * 权限共享
//     *
//     * @author FuZizheng
//     * @date 2019-03-26 15:55
//     * @param: [jobId,
//     *             fromUserId, toUserId]
//     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
//     */
//    @PostMapping("/share")
//    @ApiOperation(value = "权限共享", notes = "权限共享")
//    public ResponseMessage sharePermission(@RequestParam(required = true) Long jobId,
//            @RequestParam(required = true) List<CUser> toUserIds) {
//        Integer number = jobTasksOwnerService.sharePermission(jobId, toUserIds);
//        return ok(number);
//    }
//
//    /**
//     * 新增任务owner
//     *
//     * @param jobTasksOwner
//     * @return
//     */
//    @PostMapping
//    @ApiOperation(value = "新增", notes = "新增")
//    public ResponseMessage add(@RequestBody JobTasksOwner jobTasksOwner) {
//        Integer number = jobTasksOwnerService.insert(jobTasksOwner);
//        return ok(number);
//    }
//
//    /**
//     * 修改任务owner
//     *
//     * @param id
//     * @param jobTasksOwner
//     * @return
//     */
//    @PutMapping("/{id}")
//    @ApiOperation(value = "修改", notes = "修改")
//    public ResponseMessage update(@PathVariable("id") Long id, @RequestBody JobTasksOwner jobTasksOwner) {
//        Integer number = jobTasksOwnerService.update(id, jobTasksOwner);
//        return ok(number);
//    }
//
//    /**
//     * 删除任务owner
//     *
//     * @param id
//     * @return
//     */
//    @DeleteMapping("/{id}")
//    @ApiOperation(value = "删除", notes = "删除")
//    public ResponseMessage delete(@PathVariable("id") Long id) {
//        jobTasksOwnerService.delete(id);
//        return ok();
//    }
//
//    /**
//     * 查询明细
//     *
//     * @param id
//     * @return
//     */
//    @GetMapping("/{id}")
//    @ApiOperation(value = "查询明细", notes = "查询明细")
//    public ResponseMessage info(@PathVariable("id") Long id) {
//        JobTasksOwner jobTasksOwner = jobTasksOwnerService.selectById(id);
//        return ok(jobTasksOwner);
//    }
//
//    /**
//     * 查询列表
//     *
//     * @param pageNum
//     * @param pageSize
//     * @return
//     */
//    @GetMapping
//    @ApiOperation(value = "查询列表", notes = "查询列表")
//    public ResponseMessage list(@RequestParam(value = "pageNum", required = false) Integer pageNum,
//            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
//        Page<JobTasksOwner> page = jobTasksOwnerService.page(new Page<JobTasksOwner>(pageNum, pageSize));
//        return ok(page);
//    }
}