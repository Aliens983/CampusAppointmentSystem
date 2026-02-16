package com.laoliu.system.vo.request;

import lombok.Data;

/**
 * @author 25516
 */
@Data
public class UserRequest {

    private String name;
    private String grade;
    private String sex;
    private Integer age;
    private String email;
    private String code;
    private String password;
}
