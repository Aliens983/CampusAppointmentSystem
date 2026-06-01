package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "管理员创建用户请求")
public class AdminCreateUserRequest {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String name;

    @Schema(description = "年级", example = "大一")
    private String grade;

    @Schema(description = "性别", example = "男")
    private String sex;

    @Schema(description = "年龄", example = "18")
    private Integer age;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    private String email;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "password123")
    private String password;

    @Schema(description = "角色（1-普通用户，2-管理员，3-超级管理员）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer role;
}
