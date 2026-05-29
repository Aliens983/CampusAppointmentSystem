package com.laoliu.cas.infra.mq;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
// @Component  // RabbitMQ 已禁用
public class EmailMQConsumer {

    // @RabbitListener(queues = MqAutoConfiguration.EMAIL_QUEUE)
    public void handleEmailMessage(Map<String, String> message) {
        String to = message.get("to");
        String subject = message.get("subject");
        String content = message.get("content");

        log.info("[占位符] 收到邮件发送任务，收件人：{}，主题：{}", to, subject);
    }
}
