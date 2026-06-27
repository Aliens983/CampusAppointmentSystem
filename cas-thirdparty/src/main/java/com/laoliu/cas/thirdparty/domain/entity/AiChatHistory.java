package com.laoliu.cas.thirdparty.domain.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * AI 对话历史领域实体
 * 纯净，不依赖任何框架注解
 *
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode()
@ToString
@Builder
public class AiChatHistory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private String model;

    private String userMessage;

    private String aiResponse;

    private Integer responseTimeMs;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
