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

package cn.vbill.middleware.porter.manager.core.init;

import cn.vbill.middleware.porter.manager.core.entity.JobTasks;
import cn.vbill.middleware.porter.manager.core.entity.Nodes;
import cn.vbill.middleware.porter.manager.core.util.ApplicationContextUtil;
import cn.vbill.middleware.porter.manager.service.JobTasksService;
import cn.vbill.middleware.porter.manager.service.NodesService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author guohongjian[guo_hj@suixingpay.com]
 */
public class ResourceUtils {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    private static ResourceUtils INSTANCE;

    /**
     * NODEIDNAME_MAP
     */
    public static Map<String, String> NODEIDNAME_MAP = new HashMap<>();

    /**
     * JOBNAME_MAP
     */
    public static Map<String, String> JOBNAME_MAP = new HashMap<>();

    /**
     * getInstance
     *
     * @date 2018/8/9 下午5:22
     * @param: []
     * @return: cn.vbill.middleware.porter.manager.core.init.ResourceUtils
     */
    public static ResourceUtils getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ResourceUtils();
        }
        return INSTANCE;
    }

    /**
     * init
     *
     * @date 2018/8/9 下午5:22
     * @param: []
     * @return: void
     */
    public void init() {
        logger.info("ResourceUtils init");
        loadJobNameMap();
        loadIpNameMap();

    }

    /**
     * loadIpNameMap
     *
     * @date 2018/8/9 下午5:23
     * @param: []
     * @return: void
     */
    private void loadIpNameMap() {
        NodesService nodesService = ApplicationContextUtil.getBean(NodesService.class);
        List<Nodes> nodesList = nodesService.selectList();
        for (Nodes nodes : nodesList) {
            NODEIDNAME_MAP.put(nodes.getNodeId(), nodes.getIpAddress());
        }
    }

    /**
     * loadJobNameMap
     *
     * @date 2018/8/9 下午5:23
     * @param: []
     * @return: void
     */
    private void loadJobNameMap() {
        JobTasksService jobTasksService = ApplicationContextUtil.getBean(JobTasksService.class);
        List<JobTasks> jobTasksList = jobTasksService.selectJobNameList();
        for (JobTasks jobTasks : jobTasksList) {
            JOBNAME_MAP.put(jobTasks.getId().toString(), jobTasks.getJobName() + "-id(" + jobTasks.getId() + ")");
        }
    }

    /**
     * obtainNodeIp
     *
     * @date 2018/8/9 下午5:24
     * @param: [nodeId]
     * @return: java.lang.String
     */
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

    /**
     * obtainJobName
     *
     * @date 2018/8/9 下午5:24
     * @param: [jobId]
     * @return: java.lang.String
     */
    public static String obtainJobName(String jobId) {
        if (StringUtils.isEmpty(jobId)) {
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

    /**
     * existJob
     *
     * @date 2018/8/9 下午5:24
     * @param: [jobId]
     * @return: java.lang.Boolean
     */
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
