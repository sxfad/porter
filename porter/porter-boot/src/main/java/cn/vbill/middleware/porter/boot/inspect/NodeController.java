/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年06月28日 18:32
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
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
import java.util.Map;

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

    @GetMapping("/error")
    public String tasks() {
        Map<String, String> errors = NodeContext.INSTANCE.getTaskErrorMarked();
        return null != errors && !errors.isEmpty() ? errors.toString() : StringUtils.EMPTY;
    }

    @GetMapping("/info")
    public String info() {
        return NodeContext.INSTANCE.dumpNode();
    }

    @GetMapping("/jstack")
    public String stack() throws IOException {
        String executive = new StringBuffer(JPS_CMD).append(" ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }
    @GetMapping("/jstat")
    public String stat() throws IOException {
        String executive = new StringBuffer(JSTAT_CMD).append(" -gc ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    @GetMapping("/jinfo")
    public String jinfo() throws IOException {
        String executive = new StringBuffer(JINFO_CMD).append(" ").append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    @GetMapping("/jmap")
    public String jmap(String cmd) throws IOException {
        cmd = StringUtils.isBlank(cmd) ? "heap" : cmd;
        String subject = cmd.equals("heap") ? "heap" : "histo:live";
        String executive = new StringBuffer(JMAP_CMD).append(" ").append("-").append(subject).append(" ")
                .append(MachineUtils.CURRENT_JVM_PID).toString();
        String commandValue = StreamUtils.copyToString(Runtime.getRuntime().exec(executive).getInputStream(), Charset.forName("utf-8"));
        return formatPrint(commandValue);
    }

    private String formatPrint(String commandValue) {
        return new StringBuilder().append("<pre>").append(commandValue).append("</pre>").toString();
    }
}
