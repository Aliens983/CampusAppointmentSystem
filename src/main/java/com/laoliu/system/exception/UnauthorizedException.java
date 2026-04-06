package com.laoliu.system.exception;

/**
 * 未授权异常
 * 
 * @author 25516
 */
public class UnauthorizedException extends BusinessException {

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }
}