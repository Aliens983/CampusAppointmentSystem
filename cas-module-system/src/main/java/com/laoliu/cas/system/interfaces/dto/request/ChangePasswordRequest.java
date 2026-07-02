package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 修改密码请求
 *
 * @author forever-king
 */
@Data
@Schema(description = "修改密码请求")
public class ChangePasswordRequest {

    @Schema(description = "旧密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String oldPassword;

    @Schema(description = "新密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String newPassword;
}
