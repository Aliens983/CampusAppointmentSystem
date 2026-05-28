package com.laoliu.cas.common.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "聊天响应参数")
public class ChatRespVO {

    @Schema(description = "响应内容")
    private String response;

    @Schema(description = "响应成功与否")
    private boolean success;

    @Schema(description = "错误信息")
    private String errorMessage;

    @Schema(description = "模型名称")
    private String model;

    @Schema(description = "响应时间（毫秒）")
    private Integer responseTimeMs;

    public ChatRespVO() {}

    public ChatRespVO(String response, String model, Integer responseTimeMs) {
        this.response = response;
        this.model = model;
        this.responseTimeMs = responseTimeMs;
        this.success = true;
    }

    public ChatRespVO(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getResponseTimeMs() {
        return responseTimeMs;
    }

    public void setResponseTimeMs(Integer responseTimeMs) {
        this.responseTimeMs = responseTimeMs;
    }
}
