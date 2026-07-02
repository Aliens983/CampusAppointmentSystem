package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ServiceStatusService;
import com.laoliu.cas.appointment.interfaces.dto.response.ServiceStatusResponse;
import com.laoliu.cas.common.api.GetUserIdViaTokenApi;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Operation(summary = "获取当前用户的服务预约状态", description = "获取当前登录用户的所有预约记录")
    @GetMapping("/user")
    public CommonResult<Map<String, Object>> getServiceStatusByUser() {
        try {
            Long userId = getUserIdViaTokenApi.getUserId();
            if (userId == null) {
                return CommonResult.badRequest("无法获取用户信息，请重新登录");
            }

            List<ServiceStatusResponse> serviceStatusList = serviceStatusService.getServiceStatusByUserIdWithDescription(userId);

            Map<String, Object> result = new HashMap<>();
            result.put("serviceStatusList", serviceStatusList);
            result.put("total", serviceStatusList.size());
            return CommonResult.success(result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取所有服务预约状态", description = "获取所有用户的预约状态列表（管理端扁平路径）")
    @GetMapping
    public CommonResult<Map<String, Object>> getAllServiceStatus() {
        List<ServiceStatusResponse> serviceStatusList = serviceStatusService.getServiceStatus();
        serviceStatusList.forEach(response -> {
            if (response.getManageStatus() != null) {
                switch (response.getManageStatus()) {
                    case 0 -> response.setStatusDescription("待审核");
                    case 1 -> response.setStatusDescription("通过");
                    case 2 -> response.setStatusDescription("拒绝");
                    case 3 -> response.setStatusDescription("取消");
                    default -> response.setStatusDescription("未知状态");
                }
            }
        });
        Map<String, Object> result = new HashMap<>();
        result.put("serviceStatusList", serviceStatusList);
        result.put("total", serviceStatusList.size());
        return CommonResult.success(result);
    }
}
