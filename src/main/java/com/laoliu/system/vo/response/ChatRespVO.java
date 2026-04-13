package com.laoliu.system.vo.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 25516
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRespVO {
    private String response;
    private boolean success;
    private String errorMessage;
    private String model;
    private Integer responseTimeMs;

    // 成功响应的构造函数
    public ChatRespVO(String response, String model) {
        this.response = response;
        this.success = true;
        this.model = model;
    }

    // 成功响应的构造函数（带响应时间）
    public ChatRespVO(String response, String model, Integer responseTimeMs) {
        this.response = response;
        this.success = true;
        this.model = model;
        this.responseTimeMs = responseTimeMs;
    }

    // 错误响应的构造函数
    public ChatRespVO(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }
}