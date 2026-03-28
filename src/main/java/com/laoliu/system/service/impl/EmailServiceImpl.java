package com.laoliu.system.service.impl;

import com.laoliu.system.service.EmailSendService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author 25516
 */
@Slf4j
@Service
public class EmailServiceImpl implements EmailSendService {
    

    private final JavaMailSender javaMailSender;

    private final Environment environment;

    public EmailServiceImpl(JavaMailSender javaMailSender, Environment environment) {
        this.javaMailSender = javaMailSender;
        this.environment = environment;
    }

    @Override
    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            // 获取配置的发件人邮箱
            String fromEmail = environment.getProperty("spring.mail.username");
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            javaMailSender.send(message);
            log.info("邮件发送成功，发件人：{}，收件人：{}，主题：{}，内容：{}", fromEmail, to, subject, content);
        } catch (Exception e) {
            log.error("邮件发送失败，收件人：{}，主题：{}，错误信息：{}", to, subject, e.getMessage(), e);
            throw new RuntimeException("邮件发送失败：" + e.getMessage(), e);
        }
    }
}
