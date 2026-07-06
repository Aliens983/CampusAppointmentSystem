package com.laoliu.cas.appointment.interfaces.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务状态查询接口（扁平路径，供前端直接调用）。
 *
 * @author forever-king
 */
@Tag(name = "服务状态（用户）")
@RestController
@RequestMapping("/service-status")
@RequiredArgsConstructor
public class ServiceStatusController {

    private final ServiceStatusService serviceStatusService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

    @Operation(summary = "获取当前用户的服务预约状态（分页）", description = "分页获取当前登录用户的所有预约记录")
    @GetMapping("/user")
    public CommonResult<PageResult<ServiceStatusResponse>> getServiceStatusByUser(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            if (userId == null) {
                return CommonResult.badRequest("无法获取用户信息，请重新登录");
            }

            IPage<ServiceStatusResponse> statusPage = serviceStatusService
                    .getServiceStatusByUserIdWithDescription(userId, page, pageSize);
            return CommonResult.success(PageResult.of(statusPage));
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取所有服务预约状态（分页）", description = "分页获取所有用户的预约状态列表（管理端扁平路径）")
    @GetMapping
    public CommonResult<PageResult<ServiceStatusResponse>> getAllServiceStatus(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        IPage<ServiceStatusResponse> statusPage = serviceStatusService.getServiceStatus(page, pageSize);
        return CommonResult.success(PageResult.of(statusPage));
    }
}
