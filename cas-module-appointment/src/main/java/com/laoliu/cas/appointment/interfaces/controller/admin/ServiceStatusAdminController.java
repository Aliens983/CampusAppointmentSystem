package com.laoliu.cas.appointment.interfaces.controller.admin;

import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.interfaces.dto.request.AuditRequest;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.enums.ManageStatus;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.exception.code.ServiceStatusErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员端服务审核接口。
 *
 * @author forever-king
 */
@Tag(name = "服务审核（管理）")
@RestController
@RequestMapping("/admin/service-status")
@RequiredArgsConstructor
public class ServiceStatusAdminController {

    private final ServiceStatusService serviceStatusService;

    @Operation(summary = "获取所有服务状态（管理员专用）", description = "管理员查看所有用户的服务预约状态列表，包含待审核、通过、拒绝、取消等状态")
    @GetMapping
    public CommonResult<Map<String, Object>> getServiceStatus() {
        List<ServiceStatusResponse> serviceStatusList = serviceStatusService.getServiceStatus();
        serviceStatusList.forEach(this::setStatusDescription);
        Map<String, Object> result = new HashMap<>();
        result.put("serviceStatusList", serviceStatusList);
        result.put("total", serviceStatusList.size());
        return CommonResult.success(result);
    }

    @Operation(summary = "审核通过服务预约", description = "管理员审核通过用户的服务预约申请，审核通过后发送邮件通知申请人")
    @PostMapping("/audit/pass")
    @RequireRole({UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN})
    public CommonResult<Void> auditPass(@RequestBody AuditRequest auditRequest) {
        if (auditRequest.getStatus() == null || auditRequest.getStatus() != ManageStatus.APPROVED.getCode()) {
            return CommonResult.error(ServiceStatusErrorCode.INVALID_AUDIT_STATUS);
        }
        serviceStatusService.auditPass(auditRequest.getOrderId(), auditRequest.getReason());
        return CommonResult.success("审核通过成功", null);
    }

    @Operation(summary = "审核不通过服务预约", description = "管理员审核拒绝用户的服务预约申请，需要填写拒绝原因，审核驳回后发送邮件通知申请人")
    @PostMapping("/audit/reject")
    @RequireRole({UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN})
    public CommonResult<Void> auditReject(@RequestBody AuditRequest auditRequest) {
        if (auditRequest.getStatus() == null || auditRequest.getStatus() != ManageStatus.REJECTED.getCode()) {
            return CommonResult.error(ServiceStatusErrorCode.INVALID_AUDIT_STATUS);
        }
        if (auditRequest.getReason() == null || auditRequest.getReason().trim().isEmpty()) {
            return CommonResult.error(ServiceStatusErrorCode.AUDIT_REASON_REQUIRED);
        }
        serviceStatusService.auditReject(auditRequest.getOrderId(), auditRequest.getReason());
        return CommonResult.success("审核驳回成功", null);
    }

    private void setStatusDescription(ServiceStatusResponse response) {
        if (response.getManageStatus() != null) {
            switch (response.getManageStatus()) {
                case 0 -> response.setStatusDescription(ManageStatus.SUBMIT.getMessage());
                case 1 -> response.setStatusDescription(ManageStatus.APPROVED.getMessage());
                case 2 -> response.setStatusDescription(ManageStatus.REJECTED.getMessage());
                case 3 -> response.setStatusDescription(ManageStatus.CANCELLED.getMessage());
                default -> response.setStatusDescription("未知状态");
            }
        }
    }
}
