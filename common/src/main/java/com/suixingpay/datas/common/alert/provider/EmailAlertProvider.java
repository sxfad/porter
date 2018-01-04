/**
 * All rights Reserved, Designed By Suixingpay.
 *
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @Copyright ©2017 Suixingpay. All rights reserved.
 * 注意：本内容仅限于随行付支付有限公司内部传阅，禁止外泄以及用于其他的商业用途。
 */

package com.suixingpay.datas.common.alert.provider;

import com.suixingpay.datas.common.alert.AlertDriver;
import com.suixingpay.datas.common.alert.meta.EmailParams;
import com.suixingpay.datas.common.util.MachineUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: zhangkewei[zhang_kw@suixingpay.com]
 * @date: 2017年12月30日 11:47
 * @version: V1.0
 * @review: zhangkewei[zhang_kw@suixingpay.com]/2017年12月30日 11:47
 */
public class EmailAlertProvider implements AlertProvider {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final JavaMailSender sender;
    private final AlertDriver driver;
    private final String[] revicers;
    private final String addresser;
    public  EmailAlertProvider(AlertDriver driver, JavaMailSender sender) {
        this.driver = driver;
        this.sender = sender;
        EmailParams paramsKey = (EmailParams) driver.getAlertWay().getMeta();
        this.revicers = driver.getExtend().getOrDefault(paramsKey.RECIVER,"").split(",");
        this.addresser = driver.getExtend().getOrDefault(paramsKey.ADDRESSER, "zhang_kw@suixingpay.com");

    }

    @Override
    public boolean notice(String notice) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(addresser);
        message.setTo(revicers);
        message.setSubject("[" + DATE_FORMAT.format(new Date()) + "][" + MachineUtils.localhost() + ":" + MachineUtils.getPID() + "]数据同步告警");
        message.setText(notice);
        sender.send(message);
        return true;
    }
}
