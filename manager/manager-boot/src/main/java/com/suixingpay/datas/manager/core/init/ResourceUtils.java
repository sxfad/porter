/**
 * 
 */
package com.suixingpay.datas.manager.core.init;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<JobTasks> jobTasksList = jobTasksService.selectList();
        for (JobTasks jobTasks : jobTasksList) {
            JOBNAME_MAP.put(jobTasks.getId().toString(), jobTasks.getJobName());
        }
    }

    public static String obtainNodeIp(String nodeId) {
        String name = NODEIDNAME_MAP.get(nodeId);
        if (name == null) {
            NodesService nodesService = ApplicationContextUtil.getBean(NodesService.class);
            Nodes node = nodesService.selectByNodeId(nodeId);
            name = node == null ? "(空)" : node.getIpAddress();
            ResourceUtils.NODEIDNAME_MAP.put(nodeId, name);
        }
        return name;
    }

    public static String obtainJobName(String jobId) {
        String name = JOBNAME_MAP.get(jobId);
        if (name == null) {
            JobTasksService jobTasksService = ApplicationContextUtil.getBean(JobTasksService.class);
            JobTasks jobtask = jobTasksService.selectById(Long.valueOf(jobId));
            name = jobtask == null ? "(空)" : jobtask.getJobName();
        }
        return name;
    }
}
