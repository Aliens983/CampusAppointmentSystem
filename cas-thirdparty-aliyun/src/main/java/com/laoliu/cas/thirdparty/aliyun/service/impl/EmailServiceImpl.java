package com.laoliu.cas.thirdparty.aliyun.service.impl;

import com.laoliu.cas.common.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username:noreply@cas.com}")
    private String fromEmail;

    @Override
    @Async
    public void sendEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
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
