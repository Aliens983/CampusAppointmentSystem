package com.laoliu.cas.appointment.interfaces.controller;

import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.interfaces.dto.request.AuditRequest;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author forever-king
 */
@Tag(name = "服务状态")
@RestController
@RequestMapping("/service-status")
@RequiredArgsConstructor
public class ServiceStatusController {

    private final ServiceStatusService serviceStatusService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    @Operation(summary = "获取所有服务状态（管理员专用）")
    @GetMapping
    public CommonResult<Map<String, Object>> getServiceStatus() {
        try {
            List<ServiceStatusResponse> serviceStatusList = serviceStatusService.getServiceStatus();

            serviceStatusList.forEach(this::setStatusDescription);

            Map<String, Object> result = new HashMap<>();
            result.put("serviceStatusList", serviceStatusList);
            result.put("total", serviceStatusList.size());
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务状态失败: " + e.getMessage());
        }
    }

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
    public CommonResult<Map<String, Object>> getServiceStatusByUser() {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            if (userId == null) {
                return CommonResult.badRequest("无法获取用户信息，请重新登录");
            }

            List<ServiceStatusResponse> serviceStatusList = serviceStatusService.getServiceStatusByUserId(userId);

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

            ServiceStatusResponse serviceInfo = serviceStatusService.getServiceStatusByOrderId(auditRequest.getOrderId());
            if (serviceInfo == null) {
                return CommonResult.badRequest("服务预约不存在");
            }

            boolean success = serviceStatusService.auditService(auditRequest.getOrderId(), 1, null);
            if (success) {
                String emailContent = "您好！您的预约已通过。\n预约服务：" + serviceInfo.getServiceName()
                        + "\n服务描述：" + serviceInfo.getServiceDescribe()
                        + (auditRequest.getReason() == null ? "" : "\n备注：" + auditRequest.getReason());
                serviceStatusService.sendAuditEmail(auditRequest.getOrderId(), "预约审核通过通知", emailContent);
                return CommonResult.success("审核通过成功", null);
            } else {
                return CommonResult.badRequest("审核失败");
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
                return CommonResult.badRequest("审核原因不能为空");
            }

            ServiceStatusResponse serviceInfo = serviceStatusService.getServiceStatusByOrderId(auditRequest.getOrderId());
            if (serviceInfo == null) {
                return CommonResult.badRequest("服务预约不存在");
            }

            boolean success = serviceStatusService.auditService(auditRequest.getOrderId(), 2, auditRequest.getReason());
            if (success) {
                String emailContent = "您好！您的预约未通过。\n预约服务：" + serviceInfo.getServiceName()
                        + "\n服务描述：" + serviceInfo.getServiceDescribe()
                        + "\n拒绝原因：" + auditRequest.getReason();
                serviceStatusService.sendAuditEmail(auditRequest.getOrderId(), "预约审核未通过通知", emailContent);
                return CommonResult.success("审核不通过成功", null);
            } else {
                return CommonResult.badRequest("审核失败");
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("审核失败: " + e.getMessage());
        }
    }
}