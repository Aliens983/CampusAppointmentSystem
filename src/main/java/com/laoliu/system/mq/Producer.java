//package com.laoliu.system.mq;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AmqpTemplate;
//import org.springframework.stereotype.Component;
//
///**
// * @author 25516
// */
//@Slf4j
//@Component
//public class Producer {
//
//    private final AmqpTemplate amqpTemplate;
//
//    // 指定队列
//    private String queueName = "FightAlarmList";
//
//    public Producer(AmqpTemplate amqpTemplate) {
//        this.amqpTemplate = amqpTemplate;
//    }
//
//    public void sendMessage(String message) {
//        // 发送到指定队列
//        amqpTemplate.convertAndSend(queueName, message);
//        log.info("发送消息: " + message);
//    }
//}