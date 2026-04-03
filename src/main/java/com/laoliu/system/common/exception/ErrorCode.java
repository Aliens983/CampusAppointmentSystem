package com.laoliu.system.common.exception;

import lombok.Data;

/**
 * @author 25516
 */
@Data
public class ErrorCode {

    private final Integer code;

    private final String message;

    public ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}

