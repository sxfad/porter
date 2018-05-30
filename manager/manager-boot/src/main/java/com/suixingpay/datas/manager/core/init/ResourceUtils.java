/**
 * 
 */
package com.suixingpay.datas.manager.core.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suixingpay.datas.manager.core.entity.JobTasks;
import com.suixingpay.datas.manager.core.entity.Nodes;
import com.suixingpay.datas.manager.core.util.ApplicationContextUtil;
import com.suixingpay.datas.manager.service.JobTasksService;
import com.suixingpay.datas.manager.service.NodesService;
import com.suixingpay.datas.manager.service.impl.NodesServiceImpl;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 *
 */
public class ResourceUtils {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ResourceUtils INSTANCE;

    public static Map<String, String> NODEIDNAME_MAP = new HashMap<>();

    public static Map<String, String> JOBNAME_MAP = new HashMap<>();

    public static ResourceUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourceUtils();
        }
        return INSTANCE;
    }

    public void init() {
        logger.info("ResourceUtils init");
        loadJobNameMap();
        loadIpNameMap();

    }

    private void loadIpNameMap() {
        NodesService nodesService = ApplicationContextUtil.getBean(NodesServiceImpl.class);
        List<Nodes> nodesList = nodesService.selectList();
        for (Nodes nodes : nodesList) {
            NODEIDNAME_MAP.put(nodes.getNodeId(), nodes.getIpAddress());
        }
    }

    private void loadJobNameMap() {
        JobTasksService jobTasksService = ApplicationContextUtil.getBean(JobTasksService.class);
        List<JobTasks> jobTasksList = jobTasksService.selectJobNameList();
        for (JobTasks jobTasks : jobTasksList) {
            JOBNAME_MAP.put(jobTasks.getId().toString(), jobTasks.getJobName() + "-id(" + jobTasks.getId() + ")");
        }
    }

    public static String obtainNodeIp(String nodeId) {
        String name = NODEIDNAME_MAP.get(nodeId);
        if (name == null) {
            NodesService nodesService = ApplicationContextUtil.getBean(NodesService.class);
            Nodes node = nodesService.selectByNodeId(nodeId);
            name = node == null ? "(空-id(" + nodeId + "))" : node.getIpAddress();
            ResourceUtils.NODEIDNAME_MAP.put(nodeId, name);
        }
        return name;
    }

    public static String obtainJobName(String jobId) {
        if(StringUtils.isEmpty(jobId)) {
            return "任务id为空";
        }
        String name = JOBNAME_MAP.get(jobId);
        if (name == null) {
            JobTasksService jobTasksService = ApplicationContextUtil.getBean(JobTasksService.class);
            JobTasks jobtask = jobTasksService.selectEntityById(Long.valueOf(jobId));
            if (jobtask != null && jobtask.getJobName() != null) {
                String value = jobtask.getJobName() + "-id(" + jobId + ")";
                if (value.indexOf("空") > -1) {
                    value = "空-id(" + jobId + ")";
                }
                JOBNAME_MAP.put(jobId, value);
            }
            name = (jobtask == null || jobtask.getJobName() == null || jobtask.getJobName().indexOf("空") > -1)
                    ? "空-id(" + jobId + ")"
                    : jobtask.getJobName() + "-id(" + jobId + ")";
        }
        return name;
    }

    public static Boolean existJob(String jobId) {
        Boolean key = false;
        key = JOBNAME_MAP.containsKey(jobId);
        if (!key) {
            JobTasksService jobTasksService = ApplicationContextUtil.getBean(JobTasksService.class);
            JobTasks jobtask = jobTasksService.selectEntityById(Long.valueOf(jobId));
            if (jobtask != null && jobtask.getJobName() != null) {
                key = true;
            }
        }
        return key;
    }
}
