package com.laoliu.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置类
 * 
 * @author System
 * @since 2026-04-13
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建RestTemplate实例
     * 
     * @return RestTemplate实例
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}