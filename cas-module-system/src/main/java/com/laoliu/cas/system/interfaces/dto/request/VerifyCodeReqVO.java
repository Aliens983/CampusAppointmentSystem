package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "验证码请求类")
public class VerifyCodeReqVO {

    @Schema(description = "UUID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
}
