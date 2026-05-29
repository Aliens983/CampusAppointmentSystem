package com.laoliu.cas.infra.mq;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
// @Component  // RabbitMQ 已禁用
public class EmailMQProducer {

    // private final RabbitTemplate rabbitTemplate;

    public void sendEmailTask(String to, String subject, String content) {
        log.info("[占位符] 邮件任务已发送，收件人：{}，主题：{}", to, subject);
        // Map<String, String> message = Map.of(
        //         "to", to,
        //         "subject", subject,
        //         "content", content
        // );
        // rabbitTemplate.convertAndSend(
        //         MqAutoConfiguration.EMAIL_EXCHANGE,
        //         MqAutoConfiguration.EMAIL_ROUTING_KEY,
        //         message
        // );
        // log.info("邮件任务已发送到队列，收件人：{}，主题：{}", to, subject);
    }
}
