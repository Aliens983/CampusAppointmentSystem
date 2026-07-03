package com.laoliu.cas.appointment.interfaces.controller.admin;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.appointment.interfaces.dto.request.ServiceAddRequest;
import com.laoliu.cas.common.annotation.RequireRole;
import com.laoliu.cas.system.api.UserInfoApi;
import com.laoliu.cas.system.api.dto.UserInfoDTO;
import com.laoliu.cas.common.enums.UserRoleEnum;
import com.laoliu.cas.common.exception.code.ServiceErrorCode;
import com.laoliu.cas.common.result.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 管理员端服务管理接口。
 *
 * @author forever-king
 */
@Tag(name = "服务管理（管理）")
@RestController
@RequestMapping("/admin/service")
@RequiredArgsConstructor
public class ServiceAdminController {

    private final ServiceService serviceService;
    private final UserInfoApi userInfoApi;

    @Operation(summary = "获取所有服务", description = "获取所有可用的服务列表，仅返回状态为启用（serviceState=1）的服务")
    @GetMapping
    public CommonResult<List<Service>> getService() {
        List<Service> services = serviceService.getAllServices();
        List<Service> enabledServices = services.stream()
                .filter(Service::isAvailable)
                .toList();
        return CommonResult.success(enabledServices);
    }

    @Operation(summary = "添加服务", description = "管理员添加新的服务项目，包含服务名称、描述和状态")
    @PostMapping
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Void> addService(@Valid @RequestBody ServiceAddRequest serviceAddRequest) {
        boolean success = serviceService.addService(serviceAddRequest);
        if (success) {
            return CommonResult.success("添加服务成功", null);
        } else {
            return CommonResult.error(ServiceErrorCode.SERVICE_BOOK_FAILED);
        }
    }

    @Operation(summary = "获取指定用户的所有已预约服务", description = "管理员根据用户ID查询该用户预约的所有服务详情")
    @GetMapping("/id")
    @RequireRole(UserRoleEnum.ADMIN)
    public CommonResult<Map<String, Object>> getUserServices(
            @io.swagger.v3.oas.annotations.Parameter(description = "用户ID", required = true) @RequestParam Long userId) {
        UserInfoDTO userInfo = userInfoApi.getUserById(userId);
        if (userInfo == null) {
            return CommonResult.error(ServiceErrorCode.SERVICE_NOT_FOUND);
        }
        List<Service> services = serviceService.selectUserServices(userId);
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("user", userInfo.getName());
        result.put("userId", userId);
        result.put("userRole", userInfo.getRole());
        result.put("userGrade", userInfo.getEmail());
        result.put("services", services);
        return CommonResult.success("获取用户服务成功", result);
    }
}
