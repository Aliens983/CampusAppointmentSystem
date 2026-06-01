package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest {

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    private String email;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
    private String password;
}
