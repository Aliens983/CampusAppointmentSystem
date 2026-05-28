package com.laoliu.cas.infra.mq;

import com.laoliu.cas.common.service.EmailService;
import com.laoliu.cas.mq.config.MqAutoConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMQConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = MqAutoConfiguration.EMAIL_QUEUE)
    public void handleEmailMessage(Map<String, String> message) {
        String to = message.get("to");
        String subject = message.get("subject");
        String content = message.get("content");

        log.info("收到邮件发送任务，收件人：{}，主题：{}", to, subject);

        try {
            emailService.sendEmail(to, subject, content);
            log.info("邮件发送任务完成，收件人：{}", to);
        } catch (Exception e) {
            log.error("邮件发送任务失败，收件人：{}，错误：{}", to, e.getMessage(), e);
        }
    }
}
