package com.laoliu.cas.common.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "聊天请求类")
public class ChatReqVO {

    @Schema(description = "发送给大模型的消息")
    private String message;

    @Schema(description = "模型名称")
    private String model;

    public ChatReqVO() {}

    public ChatReqVO(String message) {
        this.message = message;
        this.model = "qwen-plus";
    }

    public ChatReqVO(String message, String model) {
        this.message = message;
        this.model = model;
    }

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
