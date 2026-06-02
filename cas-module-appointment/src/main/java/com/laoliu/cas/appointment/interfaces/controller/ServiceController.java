package com.laoliu.cas.appointment.interfaces.controller;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.infrastructure.persistence.dataobject.ServicesDO;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;
import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.system.api.UserInfoApi;
import com.laoliu.cas.system.domain.entity.User;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.exception.code.ServiceErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * @author forever-king
 */
@Tag(name = "服务管理")
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;
    private final UserInfoApi userInfoApi;

    @Operation(summary = "获取所有服务", description = "获取所有可用的服务列表，仅返回状态为启用（serviceState=1）的服务")
    @GetMapping
    public CommonResult<List<ServicesDO>> getService() {
        try {
            List<ServicesDO> services = serviceService.getAllServices();
            List<ServicesDO> enabledServices = services.stream()
                    .filter(s -> s.getServiceState() == 1)
                    .toList();
            return CommonResult.success(enabledServices);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取服务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "添加服务", description = "管理员添加新的服务项目，包含服务名称、描述和状态")
    @PostMapping
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Void> addService(@RequestBody ServiceAddRequest serviceAddRequest) {
        try {
            boolean success = serviceService.addService(serviceAddRequest);
            if (success) {
                return CommonResult.success("添加服务成功", null);
            } else {
                return CommonResult.error(ServiceErrorCode.SERVICE_BOOK_FAILED);
            }
        } catch (Exception e) {
            return CommonResult.internalServerError("添加服务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取指定用户的所有已预约服务", description = "管理员根据用户ID查询该用户预约的所有服务详情")
    @GetMapping("/id")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Map<String, Object>> getUserServices(
            @io.swagger.v3.oas.annotations.Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        User user = userInfoApi.getUserById(userId);
        if (user == null) {
            return CommonResult.error(ServiceErrorCode.SERVICE_NOT_FOUND);
        }
        try {
            List<ServicesDO> services = serviceService.selectUserServices(userId);
            Map<String, Object> result = new HashMap<>();
            result.put("user", user.getName());
            result.put("userId", userId);
            result.put("userRole", user.getRole());
            result.put("userGrade", user.getEmail());
            result.put("services", services);
            return CommonResult.success("获取用户服务成功", result);
        } catch (Exception e) {
            return CommonResult.internalServerError("获取用户服务失败：" + e.getMessage());
        }
    }
}
