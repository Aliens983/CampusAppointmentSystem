package com.laoliu.system.vo.request;

import lombok.Data;

/**
 * @author forever-king
 */
@Data
public class ResetPasswordRequest {

    private String email;

    private String code;

    private String password;
}
