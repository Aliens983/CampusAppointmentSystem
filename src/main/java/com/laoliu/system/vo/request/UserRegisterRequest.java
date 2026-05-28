package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author 25516
 */
@Schema(description = "服务添加请求类")
@Data
public class UserRegisterRequest {

    @Schema(description = "用户名" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "年级" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String grade;

    @Schema(description = "性别" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String sex;

    @Schema(description = "年龄" , requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer age;

    @Schema(description = "角色" , requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer role;

    @Schema(description = "邮箱" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "验证码" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;

    @Schema(description = "密码" , requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
