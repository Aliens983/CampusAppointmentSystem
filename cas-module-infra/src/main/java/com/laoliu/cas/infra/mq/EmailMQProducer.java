package com.laoliu.cas.infra.mq;

import com.laoliu.cas.mq.config.MqAutoConfiguration;
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
                MqAutoConfiguration.EMAIL_EXCHANGE,
                MqAutoConfiguration.EMAIL_ROUTING_KEY,
                message
        );

        log.info("邮件任务已发送到队列，收件人：{}，主题：{}", to, subject);
    }
}
