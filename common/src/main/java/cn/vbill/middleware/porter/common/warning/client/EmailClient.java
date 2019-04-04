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

import cn.vbill.middleware.porter.common.client.AbstractClient;
import cn.vbill.middleware.porter.common.warning.WarningFrequency;
import cn.vbill.middleware.porter.common.warning.config.EmailConfig;
import cn.vbill.middleware.porter.common.warning.entity.WarningMessage;
import cn.vbill.middleware.porter.common.exception.ClientConnectionException;
import cn.vbill.middleware.porter.common.warning.entity.WarningReceiver;
import com.alibaba.fastjson.JSONObject;
import com.sun.mail.util.MailSSLSocketFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:58
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:58
 */
public class EmailClient  extends AbstractClient<EmailConfig> implements WarningClient {
    private final JavaMailSender sender;
    private final WarningFrequency frequencyStat = new WarningFrequency();

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailClient.class);

    public EmailClient(EmailConfig config, int frequencyOfSecond) throws ClientConnectionException {
        super(config);

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", config.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", config.isSmtpStarttlsEnable());
        properties.put("mail.smtp.starttls.required", config.isSmtpStarttlsRequired());

        properties.put("mail.smtp.connectiontimeout", config.getConnectionTimeout());
        properties.put("mail.smtp.timeout", config.getTimeout());

        if (config.isSmtpSslEnable()) {
            properties.put("mail.smtp.ssl.enable", "true");
            //开启安全协议
            MailSSLSocketFactory sf = null;
            try {
                sf = new MailSSLSocketFactory();
                sf.setTrustAllHosts(true);
            } catch (GeneralSecurityException e1) {
                e1.printStackTrace();
            }
            properties.put("mail.smtp.ssl.socketFactory", sf);
        }
        JavaMailSenderImpl  senderImpl = new JavaMailSenderImpl();

        senderImpl.setHost(config.getHost());
        senderImpl.setUsername(config.getUsername());
        senderImpl.setPassword(config.getPassword());
        senderImpl.setJavaMailProperties(properties);
        try {
            senderImpl.testConnection();
        } catch (Exception e) {
            LOGGER.error("邮件客户端连接失败", e);
            throw new ClientConnectionException("邮件客户端连接失败:" + JSONObject.toJSONString(config));
        }
        sender = senderImpl;
        frequencyStat.setFrequencyOfSecond(frequencyOfSecond);
    }

    public EmailClient(EmailConfig config)  throws ClientConnectionException {
        this(config, 60);
    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doShutdown() {

    }

    @Override
    public void send(WarningMessage msg, WarningReceiver... receivers) {
        LOGGER.info("开始发送邮件通知.....");
        String checkContent = new StringBuffer(StringUtils.trimToEmpty(msg.getContent())).append(StringUtils.trimToEmpty(msg.getTitle()))
                .toString();
        if (!frequencyStat.canSend(checkContent)) {
            return;
        }
        LOGGER.info("判断可以发送邮件通知.....");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getConfig().getUsername());
        message.setSubject(msg.getTitle());
        message.setText(msg.getContent());

        List<String> emails = new ArrayList<>();
        Arrays.asList(receivers).stream().filter(r -> !StringUtils.isBlank(r.getEmail())).forEach(r -> emails.add(r.getEmail()));

        LOGGER.info("判断待发送邮件通知名单.....");
        if (!emails.isEmpty()) {
            message.setTo(emails.toArray(new String[0]));
            LOGGER.info("正式发送邮件.....");
            sender.send(message);
        }
        frequencyStat.updateFrequency(checkContent);
        LOGGER.info("结束发送邮件通知.....");
    }
}
