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
 * 用户端服务状态查询接口。
 *
 * @author forever-king
 */
@Tag(name = "服务状态（用户）")
@RestController
@RequestMapping("/app/service-status")
@RequiredArgsConstructor
public class ServiceStatusAppController {

    private final ServiceStatusService serviceStatusService;
    private final GetUserIdViaTokenApi getUserIdViaTokenApi;

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

    private void setStatusDescription(ServiceStatusResponse response) {
        if (response.getManageStatus() != null) {
            switch (response.getManageStatus()) {
                case 0 -> response.setStatusDescription("待审核");
                case 1 -> response.setStatusDescription("通过");
                case 2 -> response.setStatusDescription("拒绝");
                case 3 -> response.setStatusDescription("取消");
                default -> response.setStatusDescription("未知状态");
            }
        }
    }
}
