package com.laoliu.system.vo.response;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 邮件发送响应VO（视图对象）
 * 用于统一API响应格式
 * @author 25516
 */
@Getter
public class EmailResponse {
    // 响应消息
    private final String message;
    // 发送结果：true表示成功，false表示失败
    private final boolean success;
    // 响应时间戳
    private final LocalDateTime timestamp;
    // 响应数据
    private final Object data;

    // 构造函数：成功响应
    public EmailResponse(String message) {
        this.message = message;
        this.success = true;
        this.timestamp = LocalDateTime.now();
        this.data = null;
    }

    // 构造函数：带数据的成功响应
    public EmailResponse(String message, Object data) {
        this.message = message;
        this.success = true;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

    // 构造函数：失败响应
    public EmailResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
        this.timestamp = LocalDateTime.now();
        this.data = null;
    }

    // Getter方法（VO通常不提供Setter，保持不可变性）

}