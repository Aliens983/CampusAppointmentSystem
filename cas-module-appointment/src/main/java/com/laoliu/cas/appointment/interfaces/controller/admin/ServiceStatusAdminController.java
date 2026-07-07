package com.laoliu.cas.appointment.interfaces.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.interfaces.dto.request.AuditRequest;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceStatusPageReqVO;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.common.enums.ManageStatus;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.exception.code.BookErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "获取所有服务状态（管理员专用，分页+筛选）", description = "管理员分页查看所有用户的服务预约状态，支持按审核状态和服务名称筛选")
    @GetMapping
    public CommonResult<PageResult<ServiceStatusResponse>> getServiceStatus(@Valid ServiceStatusPageReqVO reqVO) {
        IPage<ServiceStatusResponse> statusPage = serviceStatusService.getServiceStatus(reqVO);
        return CommonResult.success(PageResult.of(statusPage));
    }

    @Operation(summary = "审核通过服务预约", description = "管理员审核通过用户的服务预约申请，审核通过后发送邮件通知申请人")
    @PostMapping("/audit/pass")
    @RequireRole({UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN})
    public CommonResult<Void> auditPass(@Valid @RequestBody AuditRequest auditRequest) {
        if (auditRequest.getStatus() == null || auditRequest.getStatus() != ManageStatus.APPROVED.getCode()) {
            return CommonResult.error(BookErrorCode.INVALID_AUDIT_STATUS);
        }
        serviceStatusService.auditPass(auditRequest.getOrderId(), auditRequest.getReason());
        return CommonResult.success("审核通过成功", null);
    }

    @Operation(summary = "审核不通过服务预约", description = "管理员审核拒绝用户的服务预约申请，需要填写拒绝原因，审核驳回后发送邮件通知申请人")
    @PostMapping("/audit/reject")
    @RequireRole({UserRoleEnum.ADMIN, UserRoleEnum.SUPER_ADMIN})
    public CommonResult<Void> auditReject(@Valid @RequestBody AuditRequest auditRequest) {
        if (auditRequest.getStatus() == null || auditRequest.getStatus() != ManageStatus.REJECTED.getCode()) {
            return CommonResult.error(BookErrorCode.INVALID_AUDIT_STATUS);
        }
        if (auditRequest.getReason() == null || auditRequest.getReason().trim().isEmpty()) {
            return CommonResult.error(BookErrorCode.AUDIT_REASON_REQUIRED);
        }
        serviceStatusService.auditReject(auditRequest.getOrderId(), auditRequest.getReason());
        return CommonResult.success("审核驳回成功", null);
    }
}
