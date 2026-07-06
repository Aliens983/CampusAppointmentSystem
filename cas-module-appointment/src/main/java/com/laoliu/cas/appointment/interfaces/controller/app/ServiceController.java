package com.laoliu.cas.appointment.interfaces.controller.app;

import com.laoliu.cas.appointment.application.service.ServiceService;
import com.laoliu.cas.appointment.domain.entity.Service;
import com.laoliu.cas.common.result.CommonResult;
import com.laoliu.cas.common.result.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务查询接口（扁平路径，供前端直接调用）。
 *
 * @author forever-king
 */
@Tag(name = "服务查询（用户）")
@RestController
@RequestMapping("/service")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @Operation(summary = "获取可预约服务列表（分页）", description = "分页查询所有启用状态的服务")
    @GetMapping
    public CommonResult<PageResult<Service>> getEnabledServices(
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return CommonResult.success(serviceService.getAvailableServices(page, pageSize));
    }

    @Operation(summary = "根据ID获取服务详情", description = "获取单个服务的详细信息")
    @GetMapping("/{id}")
    public CommonResult<Service> getServiceById(@PathVariable Long id) {
        return serviceService.getServiceById(id)
                .map(CommonResult::success)
                .orElse(CommonResult.notFound("服务不存在"));
    }

    @Operation(summary = "根据用户ID获取服务列表（分页）", description = "分页获取指定用户预约的所有服务")
    @GetMapping("/id")
    public CommonResult<PageResult<Service>> getServicesByUserId(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "页码，从1开始") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") int pageSize) {
        return CommonResult.success(serviceService.selectUserServices(userId, page, pageSize));
    }
}
