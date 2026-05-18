package com.laoliu.system.vo.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 服务状态响应类
 * 用于返回用户的服务预约状态信息
 * 
 * @author 25516
 */
@Schema(description = "服务状态响应类")
@Data
public class ServiceStatusResponse {
    
    /**
     * 订单ID
     */
    @Schema(description = "订单ID")
    private Long orderId;
    
    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;
    
    /**
     * 用户名
     */
    @Schema(description = "用户名")
    private String username;
    
    /**
     * 服务名称
     */
    @Schema(description = "服务名称")
    private String serviceName;
    
    /**
     * 服务描述
     */
    @Schema(description = "服务描述")
    private String serviceDescribe;
    
    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    
    /**
     * 请求状态（manage_status）
     * 0: 待审核
     * 1: 通过
     * 2: 拒绝
     * 3: 取消
     */
    @Schema(description = "请求状态（manage_status）")
    private Integer manageStatus;
    
    /**
     * 服务状态码描述
     */
    @Schema(description = "服务状态码描述")
    private String statusDescription;
}