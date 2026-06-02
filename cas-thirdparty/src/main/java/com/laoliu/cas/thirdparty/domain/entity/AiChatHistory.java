package com.laoliu.cas.thirdparty.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author forever-king
 */
@Data
@TableName("ai_chat_history")
@AllArgsConstructor
@NoArgsConstructor
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

}
