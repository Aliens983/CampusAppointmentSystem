package com.laoliu.system.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 审核请求类
 * 用于管理员审核服务预约请求
 * 
 * @author 25516
 */
@Schema(description = "审核请求类")
@Data
public class AuditRequest {
    
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long orderId;
    
    /**
     * 审核状态
     * 1: 通过
     * 2: 拒绝
     */
    @Schema(description = "审核状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    
    /**
     * 审核原因（拒绝时必填）
     */
    @Schema(description = "审核原因（拒绝时必填）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reason;
}