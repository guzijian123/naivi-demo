package com.gzj.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;



/**
 * 发送邮件
 *
 * @author yff
 */
@Component
public class EmailUtils {

    @Autowired
    private MailProperties mailProperties;

    @Autowired
    private JavaMailSender mailSender;


    /**
     * @param messageTitle 标题
     * @param sendWho      接收者
     * @param messageText  内容
     */
    public void sendEmail(String messageTitle, String sendWho, String messageText) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //发件人
        simpleMailMessage.setFrom(mailProperties.getUsername());
        //收件人
        simpleMailMessage.setTo(sendWho);
        //标题
        simpleMailMessage.setSubject(messageTitle);
        //文本
        simpleMailMessage.setText(messageText);
        mailSender.send(simpleMailMessage);

    }

}