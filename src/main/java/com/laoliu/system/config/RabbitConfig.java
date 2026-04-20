//package com.laoliu.system.config;
//
//import org.springframework.amqp.core.Queue;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author 25516
// */
//@Configuration
//public class RabbitConfig {
//
//    // 创建队列
//    @Bean
//    public Queue queue() {
//        // true 表示队列是持久化的
//        return new Queue("FighEventList", true);
//    }
//
//    // 创建队列
//    @Bean
//    public Queue queue2() {
//        // true 表示队列是持久化的
//        return new Queue("FightAlarmList", true);
//    }
//}