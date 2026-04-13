package com.laoliu.system.vo.request;

/**
 * @author 25516
 */
public class ChatReqVO {
    private String message;
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