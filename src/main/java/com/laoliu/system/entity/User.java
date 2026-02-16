package com.laoliu.system.entity;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @author 25516
 */
@Data
@Component
public class User {

    private Integer id;
    private String name;
    private String grade;
    private String sex;
    private Integer age;
    private String email;
    private String password;



}
