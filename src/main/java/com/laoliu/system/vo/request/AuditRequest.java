package com.laoliu.system.vo.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核请求类
 * 用于管理员审核服务预约请求
 * 
 * @author 25516
 */
@Data
public class AuditRequest {
    
    /**
     * 订单ID
     */
    @NotNull(message = "订单ID不能为空")
    private Long orderId;
    
    /**
     * 审核状态
     * 1: 通过
     * 2: 拒绝
     */
    @NotNull(message = "审核状态不能为空")
    private Integer status;
    
    /**
     * 审核原因（拒绝时必填）
     */
    @NotBlank(message = "拒绝原因不能为空")
    private String reason;
}