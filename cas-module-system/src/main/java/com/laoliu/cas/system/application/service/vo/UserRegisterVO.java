package com.laoliu.cas.system.application.service.vo;

import lombok.Data;

@Data
public class UserRegisterVO {

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private Integer role;

    private String email;

    private String code;

    private String password;
}
