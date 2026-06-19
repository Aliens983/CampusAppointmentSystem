package com.laoliu.cas.thirdparty.infrastructure.persistence.dataobject;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * AI 对话历史数据对象 - 用于 MyBatis-Plus ORM
 *
 * @author forever-king
 */
@Data
@TableName("ai_chat_history")
public class AiChatHistoryDO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private String model;

    private String userMessage;

    private String aiResponse;

    private Integer responseTimeMs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public com.laoliu.cas.thirdparty.domain.entity.AiChatHistory toEntity() {
        return new com.laoliu.cas.thirdparty.domain.entity.AiChatHistory(
                id, userId, model, userMessage, aiResponse, responseTimeMs, createdAt, updatedAt);
    }

    public static AiChatHistoryDO fromEntity(com.laoliu.cas.thirdparty.domain.entity.AiChatHistory entity) {
        if (entity == null) return null;
        AiChatHistoryDO dataObject = new AiChatHistoryDO();
        dataObject.setId(entity.getId());
        dataObject.setUserId(entity.getUserId());
        dataObject.setModel(entity.getModel());
        dataObject.setUserMessage(entity.getUserMessage());
        dataObject.setAiResponse(entity.getAiResponse());
        dataObject.setResponseTimeMs(entity.getResponseTimeMs());
        dataObject.setCreatedAt(entity.getCreatedAt());
        dataObject.setUpdatedAt(entity.getUpdatedAt());
        return dataObject;
    }
}
