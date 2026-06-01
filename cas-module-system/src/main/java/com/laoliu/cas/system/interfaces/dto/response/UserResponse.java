package com.laoliu.cas.system.interfaces.dto.response;

import lombok.Data;

import java.io.Serializable;

/**
 * @author forever-king
 */
@Data
public class UserResponse implements Serializable {

    private Long id;

    private String name;

    private String grade;

    private String sex;

    private Integer age;

    private String email;

    private Integer role;
}
