package com.laoliu.system.exception;

/**
 * 禁止访问异常
 * 
 * @author 25516
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}