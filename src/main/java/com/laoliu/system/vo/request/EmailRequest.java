package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 25516
 */
@Setter
@Getter
@Schema(description = "邮件请求类")
public class EmailRequest {

    // Getters and Setters
    @Schema(description = "邮件内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String to;

}