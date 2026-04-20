//package com.laoliu.system.mq;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//
///**
// * @author 25516
// */
//@Slf4j
//@Component
//public class Consumer {
//
//
//    @RabbitListener(queues = "FightAlarmList")  // 监听指定队列
//    public void receiveMessage(String message) {
//
//        log.info("接收到消息: " + message);
//        //kafkaProducerService.sendMessageAsync(message);
//
//    }
//
//
//    @RabbitListener(queues = "FighEventList")  // 监听指定队列
//    public void receiveMessage1(String message) {
//
//        log.info("接收到消息: " + message);
//
//    }
//}