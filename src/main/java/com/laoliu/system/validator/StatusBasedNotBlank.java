package com.laoliu.system.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

/**
 * 基于状态字段的必填校验注解
 * 当状态字段的值等于指定值时，该字段才必填
 * 
 * @author 25516
 */
@Documented
@Constraint(validatedBy = StatusBasedNotBlankValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface StatusBasedNotBlank {
    
    /**
     * 必填条件：当statusField的值等于statusValue时，该字段必填
     */
    String statusField();
    
    /**
     * 触发必填的状态值
     */
    int statusValue();
    
    /**
     * 校验失败时的错误消息
     */
    String message() default "字段必填";
    
    /**
     * 校验组
     */
    Class<?>[] groups() default {};
    
    /**
     * 负载
     */
    Class<? extends Payload>[] payload() default {};
}