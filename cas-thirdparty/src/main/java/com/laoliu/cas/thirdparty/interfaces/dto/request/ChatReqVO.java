package com.laoliu.cas.thirdparty.interfaces.dto.request;

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
@Schema(description = "聊天请求")
public class ChatReqVO {

    @Schema(description = "发送给大模型的消息", requiredMode = Schema.RequiredMode.REQUIRED, example = "你好，请介绍一下你自己")
    private String message;

    @Schema(description = "模型名称（可选，默认qwen-plus）", example = "qwen-plus")
    private String model;

    public ChatReqVO(String message) {
        this.message = message;
        this.model = "qwen-plus";
    }

}
