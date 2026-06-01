package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "验证码校验请求")
public class VerifyCodeReqVO {

    @Schema(description = "图形验证码UUID（从获取图形验证码接口获得）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String uuid;

    @Schema(description = "用户输入的验证码答案", requiredMode = Schema.RequiredMode.REQUIRED, example = "25")
    private String code;
}
