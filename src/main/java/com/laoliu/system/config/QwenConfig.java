package com.laoliu.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 25516
 */
@Component
@ConfigurationProperties(prefix = "qwen")
@Data
public class QwenConfig {
    private String apiKey;
    private String apiUrl = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";
    // 30秒超时
    private int timeout = 30000;
}