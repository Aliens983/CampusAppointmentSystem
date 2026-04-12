package com.laoliu.system.vo.request;

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
    private Long orderId;
    
    /**
     * 审核状态
     * 1: 通过
     * 2: 拒绝
     */
    private Integer status;
    
    /**
     * 审核原因（拒绝时必填）
     */
    private String reason;
}