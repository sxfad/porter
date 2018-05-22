package com.suixingpay.datas.manager.controller;

import static com.suixingpay.datas.manager.web.message.ResponseMessage.ok;

import java.util.List;

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

import com.suixingpay.datas.common.cluster.ClusterProviderProxy;
import com.suixingpay.datas.common.cluster.command.TaskPushCommand;
import com.suixingpay.datas.common.dic.TaskStatusType;
import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.service.JobTasksService;
import com.suixingpay.datas.manager.web.message.ResponseMessage;
import com.suixingpay.datas.manager.web.page.Page;

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
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
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
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @GetMapping
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage list(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "jobState", required = false) TaskStatusType jobState) {
        Page<JobTasks> page = jobTasksService.page(new Page<>(pageNo, pageSize), jobName, beginTime, endTime, jobState,
                1);
        return ok(page);
    }

    @GetMapping("/page")
    @ApiOperation(value = "查询分页", notes = "查询分页")
    public ResponseMessage page(@RequestParam(value = "pageNo", required = true) Integer pageNo,
            @RequestParam(value = "pageSize", required = true) Integer pageSize,
            @RequestParam(value = "jobName", required = false) String jobName,
            @RequestParam(value = "beginTime", required = false) String beginTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "jobState", required = false) TaskStatusType jobState) {
        Page<JobTasks> page = jobTasksService.page(new Page<>(pageNo, pageSize), jobName, beginTime, endTime, jobState,
                null);
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
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PostMapping
    @ApiOperation(value = "新增", notes = "新增")
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
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PutMapping
    @ApiOperation(value = "修改", notes = "修改")
    public ResponseMessage update(@RequestBody JobTasks jobTasks) {
        Integer number = jobTasksService.update(jobTasks);
        return ok(number);
    }

    /**
     * 修改任务状态
     *
     * @throws Exception
     * @author FuZizheng
     * @date 2018/3/28 上午11:27
     * @param: []
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "修改任务状态", notes = "TaskStatusType 枚举类: NEW、STOPPED、WORKING")
    public ResponseMessage updateState(@PathVariable("id") Long id,
            @RequestParam("taskStatusType") TaskStatusType taskStatusType) {
        Integer number = jobTasksService.updateState(id, taskStatusType);
        if (taskStatusType == TaskStatusType.WORKING || taskStatusType == TaskStatusType.STOPPED) {
            try {
                ClusterProviderProxy.INSTANCE
                        .broadcast(new TaskPushCommand(jobTasksService.fitJobTask(id, taskStatusType)));
            } catch (Exception e) {
                log.error("zk变更任务节点[{}]状态[{}]失败,请关注！", id, taskStatusType);
                e.printStackTrace();
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
     * @return: com.suixingpay.datas.manager.web.message.ResponseMessage
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "逻辑删除任务", notes = "参数：id")
    public ResponseMessage delete(@PathVariable("id") Long id) {
        Integer number = jobTasksService.delete(id);
        if (number == 1) {
            try {
                ClusterProviderProxy.INSTANCE
                        .broadcast(new TaskPushCommand(jobTasksService.fitJobTask(id, TaskStatusType.DELETED)));
            } catch (Exception e) {
                log.error("zk删除任务节点[{}]失败,请关注！", id);
                e.printStackTrace();
            }
        }
        return ok(number);
    }
}