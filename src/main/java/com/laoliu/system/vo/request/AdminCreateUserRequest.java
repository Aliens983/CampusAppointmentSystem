package com.laoliu.system.vo.request;

import lombok.Data;

/**
 * 超级管理员创建用户请求类
 * @author forever-king
 */
@Data
public class AdminCreateUserRequest {

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private Integer role;

    private String email;

    private String password;
}
