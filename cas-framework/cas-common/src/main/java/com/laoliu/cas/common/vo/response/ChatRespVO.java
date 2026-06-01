package com.laoliu.cas.common.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author forever-king
 */
@Schema(description = "聊天响应参数")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
