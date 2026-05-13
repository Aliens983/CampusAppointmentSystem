package com.laoliu.system.vo.response;

import lombok.Data;

/**
 * @author 25516
 */
@Data
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private String role;
    private String grade;
}
