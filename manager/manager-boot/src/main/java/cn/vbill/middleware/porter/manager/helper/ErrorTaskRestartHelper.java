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

package cn.vbill.middleware.porter.manager.helper;

import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;
import cn.vbill.middleware.porter.common.task.config.TaskConfig;
import cn.vbill.middleware.porter.common.task.dic.TaskStatusType;
import cn.vbill.middleware.porter.common.util.DefaultNamedThreadFactory;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 任务检活工具
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2019年03月11日 10:34
 * @version: V1.0
 * @review: zkevin/2019年03月11日 10:34
 */
public class ErrorTaskRestartHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorTaskRestartHelper.class);
    public static final Map<String, ImmutablePair<Calendar, Integer>> ERR_STATE = new ConcurrentHashMap<>();
    public static void run() {
        Executors.newSingleThreadScheduledExecutor(new DefaultNamedThreadFactory("task-restart-check")).schedule(() -> {
            ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                //查找任务
                String taskBasePath = AbstractClusterListener.BASE_CATALOG + "/task";
                //并行执行任务重启判断
                client.getChildren(taskBasePath).stream().parallel().forEach(t -> {
                    String taskPath = taskBasePath + "/" + t;
                    String taskErrorPath = taskPath + "/error";
                    String taskDistPath = taskPath + "/dist";
                    List<String> distList = client.getChildren(taskDistPath);
                    distList.forEach(d -> {
                        //获取任务配置
                        String taskDist = taskDistPath + "/" + d;
                        TaskConfig config =  JSON.parseObject(client.getData(taskDist).getData(), TaskConfig.class);
                        //获取异常信息
                        String errPath = taskErrorPath + "/" + d;
                        String errorStateKey = t + "_" + d;
                        if (config.getRestartRetries() > 0 && client.isExists(errPath, false)) {
                            Calendar calendar = Calendar.getInstance();
                            ImmutablePair<Calendar, Integer> triggerInfo = ERR_STATE.computeIfAbsent(errorStateKey, key -> new ImmutablePair<>(null, 0));
                            /**
                             * 任务工作中
                             * 出错任务错误类型可重启(暂未实现)
                             * 达到重启时间间隔要求
                             */
                            if (config.getStatus() == TaskStatusType.WORKING
                                    && (null == triggerInfo.getLeft() || triggerInfo.getLeft().before(calendar))
                                    && triggerInfo.right < config.getRestartRetries()) {
                                if (triggerInfo.right >= 1) {
                                    LOGGER.info("任务{}符合重启条件，触发重启操作", errorStateKey);
                                    //触发任务重启
                                    client.delete(errPath);
                                }
                                //下次触发时间
                                Calendar nextTime = null != triggerInfo.left ? triggerInfo.left : calendar;
                                nextTime.add(Calendar.SECOND, config.getRestartIncreaseBySecond() * (triggerInfo.right + 1));
                                ERR_STATE.put(errorStateKey, new ImmutablePair<>(nextTime, triggerInfo.right + 1));
                                LOGGER.info("任务{}已启动次数:{},下次重启时间:{}", errorStateKey, triggerInfo.right, null == triggerInfo.left ? "---" : triggerInfo.left.getTime().toLocaleString());
                            }
                        } else {
                            LOGGER.info("清除任务错误标记{}", errorStateKey);
                            ERR_STATE.remove(errorStateKey);
                        }
                    });
                });
            });
        }, 1, TimeUnit.MINUTES);
    }
}
