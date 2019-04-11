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

package cn.vbill.middleware.porter.boot.inspect;

import cn.vbill.middleware.porter.common.util.MachineUtils;
import cn.vbill.middleware.porter.core.NodeContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年06月28日 18:32
 * @version: V1.0
 * @review: zkevin/2018年06月28日 18:32
 */
@RestController
@RequestMapping("/inspect/node")
public class NodeController {
    private static final String JPS_CMD;
    private static final String JSTAT_CMD;
    private static final String JINFO_CMD;
    private static final String JMAP_CMD;
    static {
        String javaHome = System.getProperty("java.home", "");
        String cmd =  StringUtils.isBlank(javaHome) ? "jstack" : javaHome + "/../bin/jstack";
        JPS_CMD = cmd;
        JSTAT_CMD = StringUtils.isBlank(javaHome) ? "jstat" : javaHome + "/../bin/jstat";
        JINFO_CMD = StringUtils.isBlank(javaHome) ? "jinfo" : javaHome + "/../bin/jinfo";
        JMAP_CMD = StringUtils.isBlank(javaHome) ? "jmap" : javaHome + "/../bin/jmap";
    }

    /**
     * tasks
     *
     * @date 2018/8/9 下午3:11
     * @param: []
     * @return: java.lang.String
     */
    @GetMapping("/error")
    public String tasks() {
        List<String> errors = NodeContext.INSTANCE.getTaskErrorMarked();
        return StringUtils.join(errors, "；");
    }

    /**
     * info
     *
     * @date 2018/8/9 下午3:12
     * @param: []
     * @return: java.lang.String
     */
    @GetMapping("/info")
    public String info() {
        return NodeContext.INSTANCE.dumpNode();
    }

    /**
     * stack
     *
     * @date 2018/8/9 下午3:12
     * @param: []
     * @return: java.lang.String
     */
    @GetMapping("/jstack")
    public String stack() throws IOException {
        String executive = new StringBuffer(JPS_CMD).append(" ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    /**
     * stat
     *
     * @date 2018/8/9 下午3:12
     * @param: []
     * @return: java.lang.String
     */
    @GetMapping("/jstat")
    public String stat() throws IOException {
        String executive = new StringBuffer(JSTAT_CMD).append(" -gc ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    /**
     * jinfo
     *
     * @date 2018/8/9 下午3:12
     * @param: []
     * @return: java.lang.String
     */
    @GetMapping("/jinfo")
    public String jinfo() throws IOException {
        String executive = new StringBuffer(JINFO_CMD).append(" ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    /**
     * jmap
     *
     * @date 2018/8/9 下午3:12
     * @param: [cmd]
     * @return: java.lang.String
     */
    @GetMapping("/jmap")
    public String jmap(String cmd) throws IOException {
        cmd = StringUtils.isBlank(cmd) ? "heap" : cmd;
        String subject = cmd.equals("heap") ? "heap" : "histo:live";
        String executive = new StringBuffer(JMAP_CMD).append(" ").append("-").append(subject).append(" ")
                .append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    /**
     * formatPrint
     *
     * @date 2018/8/9 下午3:13
     * @param: [commandValue]
     * @return: java.lang.String
     */
    private String formatPrint(String commandValue) {
        return new StringBuilder().append("<pre>").append(commandValue).append("</pre>").toString();
    }
}
