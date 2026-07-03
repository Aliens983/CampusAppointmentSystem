package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户登录请求
 *
 * @author forever-king
 */
@Data
@Schema(description = "用户登录请求")
public class UserLoginRequest {

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    private String email;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
    private String password;
}
