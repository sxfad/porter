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

package cn.vbill.middleware.porter.common.warning.client;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.alibaba.fastjson.JSON;
import com.sun.mail.util.MailSSLSocketFactory;

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.cluster.ClusterProviderProxy;
import cn.vbill.middleware.porter.common.cluster.impl.AbstractClusterListener;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;
import cn.vbill.middleware.porter.common.warning.WarningFrequency;
import cn.vbill.middleware.porter.common.warning.config.EmailConfig;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;

/**
 * 通过控制台发送告警信息，先发送邮件到zk，控制台接收后发送邮件
 * 
 * @author: guohongjian[guohongjian@suixingpay.com]
 * @date: 2019年07月26日 11:48
 * @version: V1.0
 */
public class ConsoleEmailClient extends AbstractClient<EmailConfig> implements WarningClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsoleEmailClient.class);

    private final JavaMailSender sender;
    private final WarningFrequency frequencyStat = new WarningFrequency();
    // 邮箱客户端是否成功标识
    private volatile boolean islink = true;

    public ConsoleEmailClient(EmailConfig config, int frequencyOfSecond) throws ClientConnectionException {
        super(config);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", config.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", config.isSmtpStarttlsEnable());
        properties.put("mail.smtp.starttls.required", config.isSmtpStarttlsRequired());

        properties.put("mail.smtp.connectiontimeout", config.getConnectionTimeout());
        properties.put("mail.smtp.timeout", config.getTimeout());

        if (config.isSmtpSslEnable()) {
            properties.put("mail.smtp.ssl.enable", "true");
            // 开启安全协议
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e1) {
                e1.printStackTrace();
            }
            properties.put("mail.smtp.ssl.socketFactory", sf);
        }
        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

        senderImpl.setHost(config.getHost());
        senderImpl.setUsername(config.getUsername());
        senderImpl.setPassword(config.getPassword());
        senderImpl.setJavaMailProperties(properties);
        try {
            senderImpl.testConnection();
            LOGGER.info("邮件客户端测试连接成功！");
        } catch (Exception e) {
            islink = false;
            LOGGER.error("邮件客户端连接测试失败!", e.getMessage());
        }
        sender = senderImpl;
        frequencyStat.setFrequencyOfSecond(frequencyOfSecond);
    }

    public ConsoleEmailClient(EmailConfig config) throws ClientConnectionException {
        this(config, 60);
    }

    @Override
    public void send(WarningMessage msg) {
        // 如果连接没成功，转移数据到zk节点
        if (!islink && msg != null && 0 == msg.getStreamTime()) {
            LOGGER.info("邮件告警信息按照规则上传到zk节点,详细信息:[{}]", JSON.toJSONString(msg));
            try {
                // 开始1次流转
                msg.setStreamTime(1);
                // 流转到zk
                ClusterProviderProxy.INSTANCE.broadcastEvent(client -> {
                    String warnPath = AbstractClusterListener.BASE_CATALOG + "/warning/" + UUID.randomUUID().toString();
                    if (!StringUtils.isBlank(warnPath))
                        client.changeData(warnPath, false, false, JSON.toJSONString(msg));
                });
            } catch (Exception e) {
                LOGGER.error("邮件告警信息流转到zk节点失败，信息数据:[{}]", JSON.toJSONString(msg), e.getMessage());
            }
            return;
        }
        if (!islink) {
            LOGGER.warn("邮件客户端连接失败,邮件告警策略到此结束！数据信息:[{}]", JSON.toJSONString(msg));
            return;
        }
        // 正常发送邮件
        LOGGER.info("开始发送邮件通知.....");
        String checkContent = new StringBuffer(StringUtils.trimToEmpty(msg.getContent()))
                .append(StringUtils.trimToEmpty(msg.getTitle())).toString();
        if (!frequencyStat.canSend(checkContent)) {
            return;
        }
        LOGGER.info("判断可以发送邮件通知.....");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getConfig().getUsername());
        message.setSubject(msg.getTitle());
        message.setText(msg.getContent());

        List<String> emails = new ArrayList<>();
        if (null != msg.getReceiver() && null != msg.getReceiver().getOwner())
            emails.add(msg.getReceiver().getOwner().getEmail());
        if (null != msg.getReceiver() && null != msg.getReceiver().getShareOwner())
            msg.getReceiver().getShareOwner().stream().filter(r -> !StringUtils.isBlank(r.getEmail()))
                    .forEach(r -> emails.add(r.getEmail()));
        List<String> cc = new ArrayList<>();
        if (null != msg.getReceiver() && null != msg.getCopy() && !msg.getCopy().isEmpty()) {
            msg.getCopy().stream().filter(r -> !StringUtils.isBlank(r.getEmail())).forEach(r -> cc.add(r.getEmail()));
        }
        LOGGER.info("判断待发送邮件通知名单.....");
        if (!emails.isEmpty() || !cc.isEmpty()) {
            message.setTo(!emails.isEmpty() ? emails.toArray(new String[0]) : cc.toArray(new String[0]));
            if (!emails.isEmpty() && !cc.isEmpty())
                message.setCc(cc.toArray(new String[0]));
            LOGGER.info("正式发送邮件.....");
            sender.send(message);
        }
        frequencyStat.updateFrequency(checkContent);
        LOGGER.info("结束发送邮件通知.....");

    }

    @Override
    protected void doStart() throws Exception {

    }

    @Override
    protected void doShutdown() throws Exception {

    }

}
