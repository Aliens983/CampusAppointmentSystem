package com.laoliu.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 25516
 */
@Data
@Schema(description = "用户信息响应类")
public class UserResponse {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户名")
    private String name;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "用户角色")
    private String role;

    @Schema(description = "用户年级")
    private String grade;
}
