package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 25516
 */
@Schema(description = "验证码请求参数")
@Data
public class VerifyCodeReqVO {

    @Schema(description = "验证码标识符")
    private String uuid;

    @Schema(description = "验证码")
    private String code;

}
