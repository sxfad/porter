/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:58
 * @Copyright ©2018 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.client.impl;

import com.alibaba.fastjson.JSONObject;
import com.suixingpay.datas.common.alert.AlertReceiver;
import com.suixingpay.datas.common.client.AbstractClient;
import com.suixingpay.datas.common.client.AlertClient;
import com.suixingpay.datas.common.config.source.EmailConfig;
import com.suixingpay.datas.common.exception.ClientConnectionException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2018年02月23日 11:58
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2018年02月23日 11:58
 */
public class EmailClient  extends AbstractClient<EmailConfig> implements AlertClient {
    private JavaMailSender sender;
    private List<AlertReceiver> globalReceiver = new ArrayList<>();

    public EmailClient(EmailConfig config, AlertReceiver[] receiver) throws ClientConnectionException {
        super(config);

        if (null != receiver && receiver.length > 0) {
            globalReceiver.addAll(Arrays.stream(receiver).collect(Collectors.toList()));
        }

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", config.isSmtpAuth());
        properties.put("mail.smtp.starttls.enable", config.isSmtpStarttlsEnable());
        properties.put("mail.smtp.starttls.required", config.isSmtpStarttlsRequired());

        JavaMailSenderImpl  senderImpl = new JavaMailSenderImpl();

        senderImpl.setHost(config.getHost());
        senderImpl.setUsername(config.getUsername());
        senderImpl.setPassword(config.getPassword());
        senderImpl.setJavaMailProperties(properties);
        try {
            senderImpl.testConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new ClientConnectionException("邮件客户端连接失败:" + JSONObject.toJSONString(config));
        }
        sender = senderImpl;
    }

    public EmailClient(EmailConfig config)  throws ClientConnectionException {
        this(config, null);
    }

    @Override
    protected void doStart() throws IOException {
    }

    @Override
    protected void doShutdown() throws InterruptedException {

    }

    @Override
    public void send(String notice, String title, List<AlertReceiver> receivers) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(getConfig().getUsername());
        message.setSubject(title);
        message.setText(notice);

        List<String> emails = new ArrayList<>();
        if (null != receivers && !receivers.isEmpty()) {
            receivers.stream().filter(r -> !StringUtils.isBlank(r.getEmail())).forEach(r -> emails.add(r.getEmail()));
        }
        if (!globalReceiver.isEmpty()) {
            globalReceiver.stream().filter(r -> !StringUtils.isBlank(r.getEmail())).forEach(r -> emails.add(r.getEmail()));
        }

        if (!receivers.isEmpty()) {
            message.setTo(receivers.toArray(new String[0]));
            sender.send(message);
        }
    }
}
