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

package cn.vbill.middleware.porter.plugin.connector.jdbc.client;

import cn.vbill.middleware.porter.common.task.consumer.ConsumeClient;
import cn.vbill.middleware.porter.common.task.consumer.Position;
import cn.vbill.middleware.porter.common.task.exception.TaskStopTriggerException;
import cn.vbill.middleware.porter.plugin.connector.jdbc.config.JdbcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 */
public class JdbcConsumeClient extends JdbcClient implements ConsumeClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConsumeClient.class);

    public JdbcConsumeClient(JdbcConfig config) {
        super(config);
    }

    @Override
    public boolean isAutoCommitPosition() {
        return false;
    }

    @Override
    public long commitPosition(Position position) throws TaskStopTriggerException {
        return 0;
    }

    @Override
    public void initializePosition(String taskId, String swimlaneId, String position) throws TaskStopTriggerException {

    }

    @Override
    public String getSwimlaneId() {
        return null;
    }

    @Override
    public <F, O> List<F> fetch(FetchCallback<F, O> callback) throws TaskStopTriggerException, InterruptedException {
        return null;
    }

    @Override
    public String getInitiatePosition(String offset) {
        return null;
    }
}