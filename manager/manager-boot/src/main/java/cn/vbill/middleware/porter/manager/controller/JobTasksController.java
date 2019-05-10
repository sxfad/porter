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

import static cn.vbill.middleware.porter.manager.web.message.ResponseMessage.ok;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.event.command.TaskPushCommand;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.warning.entity.WarningOwner;
import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.service.JobTasksService;
import cn.vbill.middleware.porter.manager.web.message.ResponseMessage;
import cn.vbill.middleware.porter.manager.web.page.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 同步任务表 controller控制器
 *
 * @author: FairyHood
 * @date: 2018-03-07 17:26:55
 * @version: V1.0-auto
 * @review: FairyHood/2018-03-07 17:26:55
 */
@Api(description = "同步任务表管理")
@RestController
@RequestMapping("/manager/jobtasks")
public class JobTasksController {

    private Logger log = LoggerFactory.getLogger(JobTasksController.class);

    @Autowired
    protected JobTasksService jobTasksService;

    /**
     * 查询明细
     *
     * @author FuZizheng
     * @date 2018/3/26 下午1:51
     * @param: [id]
     * @return: ResponseMessage
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "查询明细", notes = "查询明细")
    public ResponseMessage info(@PathVariable("id") Long id) {
        JobTasks jobTasks = jobTasksService.selectById(id);
        return ok(jobTasks);
    }

    /**
     * 查询分页
     *
     * @author FuZizheng
     * @date 2018/3/26 上午11:41
     * @param: [pageNo,
     *             pageSize, jobName, beginTime, endTime, jobState]
     * @return: ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "jobState", required = false) TaskStatusType jobState,
            @RequestParam(value = "id", required = false) Long id) {
        Page<JobTasks> page = jobTasksService.page(new Page<>(pageNo, pageSize), jobName, jobId, beginTime, endTime, jobState,
                1, id);
        return ok(page);
    }

    /**
     * 分页
     *
     * @author FuZizheng
     * @date 2018/8/9 下午4:23
     * @param: [pageNo,
     *             pageSize, jobName, beginTime, endTime, jobState, jobType]
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/page")
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage page(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "jobId", required = false) Long jobId,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "jobState", required = false) TaskStatusType jobState,
            @RequestParam(value = "jobType", required = false) Integer jobType,
            @RequestParam(value = "id", required = false) Long id) {
        Page<JobTasks> page = jobTasksService.page(new Page<>(pageNo, pageSize), jobName, jobId, beginTime, endTime, jobState,
                jobType, id);
        return ok(page);
    }

    /**
     * 数据表组表名数组
     *
     * @param tablesId
     * @return
     */
    @GetMapping(value = "tablenames")
    @ApiOperation(value = "数据表组表名数组", notes = "数据表组表名数组")
    public ResponseMessage tableNames(
            @RequestParam(value = "tablesId", required = true) @ApiParam(value = "数据表组id") Long tablesId) {
        Object o = jobTasksService.tableNames(tablesId);
        return ok(o);
    }

    /**
     * 查询表字段
     *
     * @param sourceId
     * @param tablesId
     * @param tableAllName
     * @return
     */
    @GetMapping(value = "fields")
    @ApiOperation(value = "查询表字段", notes = "查询表字段")
    public ResponseMessage fields(
            @RequestParam(value = "sourceId", required = true) @ApiParam(value = "数据源id") Long sourceId,
            @RequestParam(value = "tablesId", required = false) @ApiParam(value = "数据表组id") Long tablesId,
            @RequestParam(value = "tableAllName", required = true) @ApiParam(value = "数据表全名") String tableAllName) {
        List<String> fields = jobTasksService.fields(sourceId, tablesId, tableAllName);
        return ok(fields);
    }

    /**
     * 新增
     *
     * @author FuZizheng
     * @date 2018/3/27 下午3:17
     * @param: [jobTasks]
     * @return: ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增普通任务", notes = "新增普通任务")
    public ResponseMessage add(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.insert(jobTasks);
        return ok(number);
    }

    /**
     * 修改
     *
     * @author FuZizheng
     * @date 2018/4/2 下午4:29
     * @param: [jobTasks]
     * @return: ResponseMessage
     */
    @PutMapping
    @ApiOperation(value = "修改普通任务", notes = "修改普通任务")
    public ResponseMessage update(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.update(jobTasks);
        return ok(number);
    }

    /**
     * 新增特殊任务
     * 
     * @param jobTasks
     * @return
     */
    @PostMapping("/addspecial")
    @ApiOperation(value = "新增特殊任务", notes = "新增特殊任务")
    public ResponseMessage addSpecial(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.insertZKCapture(jobTasks, TaskStatusType.NEW);
        return ok(number);
    }

    /**
     * 修改特殊任务
     * 
     * @param jobTasks
     * @return
     */
    @PutMapping("/updatespecial")
    @ApiOperation(value = "修改特殊任务", notes = "修改特殊任务")
    public ResponseMessage updateSpecial(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.updateZKCapture(jobTasks, TaskStatusType.NEW);
        return ok(number);
    }

    /**
     * 解析特殊配置
     * 
     * @param jobTasks
     * @return
     */
    @PostMapping(value = "/dealspecialjson")
    @ApiOperation(value = "解析字符串", notes = "解析字符串")
    public ResponseMessage dealSpecialJson(@RequestBody JobTasks jobTasks) {
        String jobXmlText = jobTasks.getJobXmlText();
        log.info("传入字符串:[{}]", jobXmlText);
        try {
            String taskConfigJson = java.net.URLDecoder.decode(jobXmlText, "UTF-8");
            log.info("转移后字符串:[{}]", taskConfigJson);
            TaskConfig taskConfig = jobTasksService.dealSpecialJson(taskConfigJson);
            log.info("解析后字符串:[{}]", JSON.toJSON(taskConfig));
            return ok(taskConfig);
        } catch (UnsupportedEncodingException e) {
            log.error("输入数据解析失败!", e);
            return ResponseMessage.error("输入数据解析失败!");
        }
    }

    /**
     * 修改任务状态
     *
     * @throws Exception
     * @author FuZizheng
     * @date 2018/3/28 上午11:27
     * @param: []
     * @return: ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改任务状态", notes = "TaskStatusType 枚举类: NEW、STOPPED、WORKING")
    public ResponseMessage updateState(@PathVariable("id") Long id,
            @RequestParam("taskStatusType") TaskStatusType taskStatusType) {
        Integer number = jobTasksService.updateState(id, taskStatusType);
        if (taskStatusType == TaskStatusType.WORKING || taskStatusType == TaskStatusType.STOPPED) {
            //任务信息
            try {
                TaskPushCommand config = new TaskPushCommand(jobTasksService.fitJobTask(id, taskStatusType));
                ClusterProviderProxy.INSTANCE.broadcastEvent(config);
                log.info("zk任务Id:[{}] 状态:[{}] 详情:[{}].", id, taskStatusType, JSON.toJSONString(config));
            } catch (Exception e) {
                log.error("zk变更任务Id[{}] 状态[{}]失败,请关注！", id, taskStatusType);
                return ResponseMessage.error("变更任务失败！");
            }
            //权限信息(附邮箱)
            try {
                WarningOwner warningOwner = jobTasksService.selectJobWarningOwner(id);
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
        }
        return ok(number);
    }

    /**
     * 逻辑删除任务
     *
     * @author FuZizheng
     * @date 2018/3/28 上午11:50
     * @param: [id]
     * @return: ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除任务", notes = "参数：id")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = jobTasksService.delete(id);
        if (number == 1) {
            //任务信息
            try {
                ClusterProviderProxy.INSTANCE
                        .broadcastEvent(new TaskPushCommand(jobTasksService.fitJobTask(id, TaskStatusType.DELETED)));
            } catch (Exception e) {
                log.error("zk删除任务节点[{}]失败,请关注！", id, e);
                return ResponseMessage.error("zk删除任务节点失败！");
            }
            //权限信息(附邮箱)
            try {
                ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                    String nodePath = AbstractClusterListener.BASE_CATALOG + "/task/" + id + "/owner";
                    if (!StringUtils.isBlank(nodePath)) {
                        client.delete(nodePath);
                    }
                });
                log.info("zk删除任务id[{}]权限数据成功!", id);
            } catch (Exception e) {
                log.error("zk删除任务id[{}]权限数据失败,请关注！", id, e);
                return ResponseMessage.error("删除任务权限失败，请关注！");
            }
        }
        return ok(number);
    }

    /**
     * 显示任务id下拉框
     *
     * @author FuZizheng
     * @date 2019/2/19 2:07 PM
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.web.message.ResponseMessage
     */
    @GetMapping("/showjobIdList")
    @ApiOperation(value = "显示任务id", notes = "显示任务id")
    public ResponseMessage jobIdList() {
        List<Long> jobIds = jobTasksService.showjobIdList();
        return ok(jobIds);
    }

}
