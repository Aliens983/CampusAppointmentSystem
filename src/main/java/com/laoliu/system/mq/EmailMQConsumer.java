package com.laoliu.system.mq;

import com.laoliu.system.config.RabbitMQConfig;
import com.laoliu.system.service.EmailSendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMQConsumer {

    private final EmailSendService emailSendService;

    @RabbitListener(queues = RabbitMQConfig.EMAIL_QUEUE)
    public void handleEmailMessage(Map<String, String> message) {
        String to = message.get("to");
        String subject = message.get("subject");
        String content = message.get("content");

        log.info("收到邮件发送任务，收件人：{}，主题：{}", to, subject);

        try {
            emailSendService.sendEmail(to, subject, content);
            log.info("邮件发送任务完成，收件人：{}", to);
        } catch (Exception e) {
            log.error("邮件发送任务失败，收件人：{}，错误：{}", to, e.getMessage(), e);
        }
    }
}
