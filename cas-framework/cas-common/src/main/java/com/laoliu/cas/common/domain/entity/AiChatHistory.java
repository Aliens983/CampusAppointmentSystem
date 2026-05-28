package com.laoliu.cas.common.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("ai_chat_history")
public class AiChatHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String model;

    private String userMessage;

    private String aiResponse;

    private Integer responseTimeMs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public AiChatHistory(Long id, Long userId, String model, String userMessage, String aiResponse, Integer responseTimeMs, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.model = model;
        this.userMessage = userMessage;
        this.aiResponse = aiResponse;
        this.responseTimeMs = responseTimeMs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
