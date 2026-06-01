package com.laoliu.cas.common.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author forever-king
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "聊天请求类")
public class ChatReqVO {

    @Schema(description = "发送给大模型的消息")
    private String message;

    @Schema(description = "模型名称")
    private String model;

    public ChatReqVO(String message) {
        this.message = message;
        this.model = "qwen-plus";
    }

}
