package com.laoliu.system.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * StatusBasedNotBlank校验器
 * 当状态字段的值等于指定值时，校验目标字段是否非空
 * 
 * @author 25516
 */
public class StatusBasedNotBlankValidator implements ConstraintValidator<StatusBasedNotBlank, String> {
    
    private String statusField;
    private int statusValue;
    
    @Override
    public void initialize(StatusBasedNotBlank constraintAnnotation) {
        this.statusField = constraintAnnotation.statusField();
        this.statusValue = constraintAnnotation.statusValue();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果值为空，检查是否需要必填
        if (value == null || value.trim().isEmpty()) {
            // 获取当前对象
            Object current = context.getRootBean();
            if (current instanceof AuditRequest) {
                AuditRequest request = (AuditRequest) current;
                // 获取status字段的值
                Integer status = request.getStatus();
                // 如果status等于指定值（2-拒绝），则reason必填
                // 返回false表示校验失败（reason为空但需要必填）
                return !(status != null && status == statusValue);
            }
            // 如果类型不匹配，默认认为必填
            return false;
        }
        // 值不为空，校验通过
        return true;
    }
}