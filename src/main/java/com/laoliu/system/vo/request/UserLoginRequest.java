package com.laoliu.system.vo.request;

import lombok.Data;

/**
 * @author forever-king
 */
@Data
public class UserLoginRequest {

    private String email;
    private String password;
}
