package com.laoliu.system.controller;

import com.laoliu.system.annotation.RequireRole;
import com.laoliu.system.api.GetUserIdViaTokenApi;
import com.laoliu.system.common.exception.enums.ServiceStatusErrorCode;
import com.laoliu.system.common.result.CommonResult;
import com.laoliu.system.enums.UserRoleEnum;
import com.laoliu.system.mapper.ItemMapper;
import com.laoliu.system.service.EmailSendService;
import com.laoliu.system.vo.request.AuditRequest;
import com.laoliu.system.vo.response.ServiceStatusResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 服务状态控制器
 * 提供服务预约状态查询功能
 * 
 * @author 25516
 */
@RestController
@RequestMapping("/service-status")
public class ServiceStatusController {

    private final ItemMapper itemMapper;
    private final EmailSendService emailSendService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    public ServiceStatusController(ItemMapper itemMapper, EmailSendService emailSendService, GetUserIdViaTokenApi getUserIdViaTokenApi) {
        this.itemMapper = itemMapper;
        this.emailSendService = emailSendService;
        this.getUserIdViaTokenApi = getUserIdViaTokenApi;
    }

    @Operation(summary = "获取所有服务状态（管理员专用）")
    @GetMapping
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Map<String, Object>> getServiceStatus(HttpServletRequest request) {
        try {
            List<ServiceStatusResponse> serviceStatusList = itemMapper.getServiceStatus();
            
            // 设置状态描述
            serviceStatusList.forEach(this::setStatusDescription);
            
            Map<String, Object> result = new HashMap<>();
            result.put("serviceStatusList", serviceStatusList);
            result.put("total", serviceStatusList.size());
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务状态失败: " + e.getMessage());
        }
    }

    /**
     * 设置服务状态描述
     */
    private void setStatusDescription(ServiceStatusResponse response) {
        if (response.getManageStatus() != null) {
            switch (response.getManageStatus()) {
                case 0:
                    response.setStatusDescription("待审核");
                    break;
                case 1:
                    response.setStatusDescription("通过");
                    break;
                case 2:
                    response.setStatusDescription("拒绝");
                    break;
                case 3:
                    response.setStatusDescription("取消");
                    break;
                default:
                    response.setStatusDescription("未知状态");
            }
        }
    }

    @Operation(summary = "获取用户自己的服务状态")
    @GetMapping("/user")
    public CommonResult<Map<String, Object>> getServiceStatusByUser(HttpServletRequest request) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId(request);
            if (userId == null) {
                return CommonResult.badRequest("无法获取用户信息，请重新登录");
            }
            
            List<ServiceStatusResponse> serviceStatusList = itemMapper.getServiceStatusByUserId(userId);
            
            // 设置状态描述
            serviceStatusList.forEach(this::setStatusDescription);
            
            Map<String, Object> result = new HashMap<>();
            result.put("serviceStatusList", serviceStatusList);
            result.put("total", serviceStatusList.size());
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "审核通过服务预约")
    @PostMapping("/audit/pass")
    public CommonResult<Void> auditPass(@RequestBody AuditRequest auditRequest) {
        try {
            if (auditRequest.getStatus() == null || auditRequest.getStatus() != 1) {
                return CommonResult.badRequest("审核状态无效");
            }
            
            ServiceStatusResponse serviceInfo = itemMapper.getServiceStatusByOrderId(auditRequest.getOrderId());
            if (serviceInfo == null) {
                return CommonResult.badRequest("服务预约不存在");
            }
            
            int rows = itemMapper.auditService(auditRequest.getOrderId(), 1, null);
            if (rows > 0) {
                String emailContent = "您好！您的预约已通过。预约服务：" + serviceInfo.getServiceName();
                emailSendService.sendEmail(getUserEmail(serviceInfo.getUserId()), "预约审核通过通知", emailContent);
                return CommonResult.success("审核通过成功", null);
            } else {
                return CommonResult.error(ServiceStatusErrorCode.AUDIT_FAILED);
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("审核失败: " + e.getMessage());
        }
    }

    @Operation(summary = "审核不通过服务预约")
    @PostMapping("/audit/reject")
    public CommonResult<Void> auditReject(@RequestBody AuditRequest auditRequest) {
        try {
            if (auditRequest.getStatus() == null || auditRequest.getStatus() != 2) {
                return CommonResult.badRequest("审核状态无效");
            }
            
            if (auditRequest.getReason() == null || auditRequest.getReason().trim().isEmpty()) {
                return CommonResult.badRequest("拒绝原因不能为空");
            }
            
            ServiceStatusResponse serviceInfo = itemMapper.getServiceStatusByOrderId(auditRequest.getOrderId());
            if (serviceInfo == null) {
                return CommonResult.badRequest("服务预约不存在");
            }
            
            int rows = itemMapper.auditService(auditRequest.getOrderId(), 2, auditRequest.getReason());
            if (rows > 0) {
                String emailContent = "您好！您的预约未通过。\n原因如下：\n" + auditRequest.getReason();
                emailSendService.sendEmail(getUserEmail(serviceInfo.getUserId()), "预约审核结果通知", emailContent);
                return CommonResult.success("审核不通过成功", null);
            } else {
                return CommonResult.error(ServiceStatusErrorCode.AUDIT_FAILED);
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("审核失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户邮箱（从数据库查询）
     * 
     * @param orderId 订单ID
     * @return 用户邮箱
     */
    private String getUserEmail(Long orderId) {
        String email = itemMapper.getUserEmailByOrderId(orderId);
        return email != null ? email : "user" + orderId + "@example.com";
    }
}
