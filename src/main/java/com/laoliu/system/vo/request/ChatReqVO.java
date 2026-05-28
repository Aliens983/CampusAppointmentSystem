package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * @author 25516
 */
@Schema(description = "聊天请求类")
public class ChatReqVO {

    @Schema(description = "发送给大模型的消息")
    private String message;

    @Schema(description = "模型名称")
    private String model;

    // 默认构造函数
    public ChatReqVO() {}

    // 带参构造函数
    public ChatReqVO(String message) {
        this.message = message;
        // 默认使用qwen-plus模型
        this.model = "qwen-plus";
    }
    public ChatReqVO(String message, String model) {
        this.message = message;
        this.model = model;
    }

    // Getter 和 Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}