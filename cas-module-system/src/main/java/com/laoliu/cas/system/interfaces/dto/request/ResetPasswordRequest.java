package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "重置密码请求")
public class ResetPasswordRequest {

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    private String email;

    @Schema(description = "邮箱验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String code;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "newPassword123")
    private String password;
}
