package com.laoliu.system.exception;

/**
 * 资源不存在异常
 * 
 * @author 25516
 */
public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}