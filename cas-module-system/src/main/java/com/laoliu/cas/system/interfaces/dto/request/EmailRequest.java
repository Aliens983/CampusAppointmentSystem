package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "邮件请求类")
public class EmailRequest {

    @Schema(description = "收件人邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    private String to;
}
