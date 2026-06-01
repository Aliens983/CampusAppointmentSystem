package com.laoliu.cas.system.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author forever-king
 */
@Data
@Schema(description = "管理员创建用户请求")
public class AdminCreateUserRequest {

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private String email;

    private String password;

    private Integer role;
}
