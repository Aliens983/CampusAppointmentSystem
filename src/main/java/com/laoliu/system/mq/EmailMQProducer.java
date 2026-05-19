package com.laoliu.system.mq;

import com.laoliu.system.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailMQProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailTask(String to, String subject, String content) {
        Map<String, String> message = Map.of(
                "to", to,
                "subject", subject,
                "content", content
        );

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EMAIL_EXCHANGE,
                RabbitMQConfig.EMAIL_ROUTING_KEY,
                message
        );

        log.info("邮件任务已发送到队列，收件人：{}，主题：{}", to, subject);
    }
}
