package com.laoliu.system.exception;

import com.laoliu.system.common.exception.ErrorCode;
import com.laoliu.system.common.result.CommonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 全局异常处理器
 * 
 * @author 25516
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理数据库异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleDataAccessException(DataAccessException e) {
        log.error("数据库访问异常: ", e);
        return CommonResult.internalServerError("数据库操作失败: " + e.getMessage());
    }

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleBusinessException(BusinessException e, HttpServletRequest request) {
        log.warn("业务异常: {}", e.getMessage());
        ErrorCode errorCode = e.getErrorCode();
        if (errorCode != null) {
            // 根据错误码的code值设置对应的HTTP状态码
            HttpStatus status = HttpStatus.valueOf(errorCode.getCode());
            log.info("业务异常状态码: {}, 错误信息: {}", status.value(), errorCode.getMessage());
            return CommonResult.error(errorCode);
        }
        return CommonResult.internalServerError(e.getMessage());
    }

    /**
     * 处理未授权异常
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public CommonResult<?> handleUnauthorizedException(UnauthorizedException e) {
        log.warn("未授权访问: {}", e.getMessage());
        return CommonResult.unauthorized(e.getMessage());
    }

    /**
     * 处理禁止访问异常
     */
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public CommonResult<?> handleForbiddenException(ForbiddenException e) {
        log.warn("禁止访问: {}", e.getMessage());
        return CommonResult.forbidden(e.getMessage());
    }

    /**
     * 处理资源不存在异常
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonResult<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        log.warn("资源不存在: {}", e.getMessage());
        return CommonResult.notFound(e.getMessage());
    }

    /**
     * 处理参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("参数异常: {}", e.getMessage());
        return CommonResult.badRequest(e.getMessage());
    }

    /**
     * 处理运行时异常
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        log.error("运行时异常: ", e);
        String requestUri = request.getRequestURI();
        log.error("请求URL: {}", requestUri);
        return CommonResult.internalServerError("服务器内部错误: " + e.getMessage());
    }

    /**
     * 处理参数校验异常 (MethodArgumentNotValidException)
     * 用于处理 @Valid 注解触发的校验
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError error : allErrors) {
            message.append(error.getDefaultMessage()).append("; ");
        }
        return CommonResult.badRequest(message.toString());
    }

    /**
     * 处理参数校验异常 (BindException)
     * 用于处理 @Validated 注解触发的校验
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult<?> handleBindException(BindException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        StringBuilder message = new StringBuilder();
        for (ObjectError error : allErrors) {
            message.append(error.getDefaultMessage()).append("; ");
        }
        return CommonResult.badRequest(message.toString());
    }

    /**
     * 处理所有异常的兜底方法
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleException(Exception e, HttpServletRequest request) {
        log.error("全局异常处理: ", e);
        String requestUri = request.getRequestURI();
        log.error("请求URL: {}", requestUri);
        return CommonResult.internalServerError("服务器内部错误: " + e.getMessage());
    }

    /**
     * 处理IO异常
     */
    @ExceptionHandler(java.io.IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleIOException(java.io.IOException e) {
        log.error("IO异常: ", e);
        return CommonResult.internalServerError("文件操作异常: " + e.getMessage());
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public CommonResult<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常: ", e);
        return CommonResult.internalServerError("系统异常: " + e.getMessage());
    }
}